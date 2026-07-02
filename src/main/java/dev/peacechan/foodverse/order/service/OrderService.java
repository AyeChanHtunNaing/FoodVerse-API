package dev.peacechan.foodverse.order.service;

import dev.peacechan.foodverse.common.exception.BadRequestException;
import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.config.CacheNames;
import dev.peacechan.foodverse.customer.entity.CustomerProfile;
import dev.peacechan.foodverse.menu.entity.Menu;
import dev.peacechan.foodverse.order.entity.Order;
import dev.peacechan.foodverse.order.entity.OrderItem;
import dev.peacechan.foodverse.restaurant.entity.Restaurant;
import dev.peacechan.foodverse.auth.entity.User;
import dev.peacechan.foodverse.menu.enums.MenuStatus;
import dev.peacechan.foodverse.order.enums.OrderStatus;
import dev.peacechan.foodverse.restaurant.enums.RestaurantStatus;
import dev.peacechan.foodverse.auth.enums.UserRole;
import dev.peacechan.foodverse.email.service.EmailService;
import dev.peacechan.foodverse.order.dto.CreateOrderItemRequest;
import dev.peacechan.foodverse.order.dto.CreateOrderRequest;
import dev.peacechan.foodverse.order.dto.OrderResponse;
import dev.peacechan.foodverse.order.dto.UpdateOrderStatusRequest;
import dev.peacechan.foodverse.order.mapper.OrderMapper;
import dev.peacechan.foodverse.menu.repository.MenuRepository;
import dev.peacechan.foodverse.order.repository.OrderRepository;
import dev.peacechan.foodverse.restaurant.repository.RestaurantRepository;
import dev.peacechan.foodverse.auth.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;

    @Transactional
    public OrderResponse placeOrder(String email, CreateOrderRequest request) {
        User user = getCustomerUserByEmail(email);
        CustomerProfile customerProfile = getCustomerProfile(user);
        Restaurant restaurant = getOpenRestaurantById(request.restaurantId());
        validateNoDuplicateMenus(request.items());

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .status(OrderStatus.PLACED)
                .orderedAt(LocalDateTime.now())
                .customerProfile(customerProfile)
                .restaurant(restaurant)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Menu menu = getAvailableMenu(itemRequest.menuId(), restaurant.getId());
            OrderItem orderItem = orderMapper.toOrderItem(order, menu, itemRequest.quantity());
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getLineTotal());
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        emailService.sendOrderCreatedEmail(
                user.getEmail(),
                user.getFullName(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount(),
                restaurant.getId()
        );
        OrderResponse response = orderMapper.toResponse(savedOrder);
        evictOrderCaches(response.id(), email);
        return response;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.OWN_ORDERS, key = "#email + '::' + #orderId")
    public OrderResponse getOwnOrder(Long orderId, String email) {
        return orderMapper.toResponse(getOrderByIdAndOwner(orderId, email));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.ORDERS, key = "#orderId")
    public OrderResponse getOrder(Long orderId) {
        return orderMapper.toResponse(getOrderById(orderId));
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.ORDERS, key = "#orderId")
    public OrderResponse cancelOwnOrder(Long orderId, String email) {
        Order order = getOrderByIdAndOwner(orderId, email);
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new BadRequestException("Only placed orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        OrderResponse response = orderMapper.toResponse(orderRepository.save(order));
        evictOrderCaches(orderId, email);
        return response;
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNames.ORDERS, key = "#orderId")
    public OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = getOrderById(orderId);
        validateStatusTransition(order.getStatus(), request.status());
        order.setStatus(request.status());
        Order savedOrder = orderRepository.save(order);
        User customer = order.getCustomerProfile().getUser();
        String customerEmail = customer.getEmail();
        emailService.sendOrderUpdatedEmail(
                customerEmail,
                customer.getFullName(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount()
        );
        OrderResponse response = orderMapper.toResponse(savedOrder);
        evictOrderCaches(orderId, customerEmail);
        return response;
    }

    private void evictOrderCaches(Long orderId, String email) {
        redisTemplate.delete(Set.of(
                CacheNames.ORDERS + "::" + orderId,
                CacheNames.OWN_ORDERS + "::" + email + "::" + orderId
        ));
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == newStatus) {
            throw new BadRequestException("Order is already in status: " + newStatus.name());
        }

        EnumSet<OrderStatus> allowedStatuses = switch (currentStatus) {
            case PLACED -> EnumSet.of(OrderStatus.PREPARING, OrderStatus.CANCELLED);
            case PREPARING -> EnumSet.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
            case DELIVERED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
        };

        if (!allowedStatuses.contains(newStatus)) {
            throw new BadRequestException(
                    "Invalid order status transition from " + currentStatus.name() + " to " + newStatus.name()
            );
        }
    }

    private void validateNoDuplicateMenus(List<CreateOrderItemRequest> items) {
        Set<Long> menuIds = new HashSet<>();
        for (CreateOrderItemRequest item : items) {
            if (!menuIds.add(item.menuId())) {
                throw new BadRequestException("Duplicate menu items are not allowed in one order");
            }
        }
    }

    private User getCustomerUserByEmail(String email) {
        return userRepository.findByEmailAndRole(email, UserRole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
    }

    private CustomerProfile getCustomerProfile(User user) {
        if (user.getCustomerProfile() == null) {
            throw new BadRequestException("Customer profile is required before placing an order");
        }
        return user.getCustomerProfile();
    }

    private Restaurant getOpenRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        if (restaurant.getStatus() != RestaurantStatus.OPEN) {
            throw new BadRequestException("Restaurant is not open for ordering");
        }

        return restaurant;
    }

    private Menu getAvailableMenu(Long menuId, Long restaurantId) {
        Menu menu = menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu not found with id: " + menuId + " in restaurant: " + restaurantId
                ));

        if (menu.getStatus() != MenuStatus.AVAILABLE) {
            throw new BadRequestException("Menu is not available for ordering: " + menu.getName());
        }

        return menu;
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private Order getOrderByIdAndOwner(Long orderId, String email) {
        return orderRepository.findByIdAndCustomerProfileUserEmail(orderId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
