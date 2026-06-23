package dev.peacechan.foodverse.order.mapper;

import dev.peacechan.foodverse.entity.Menu;
import dev.peacechan.foodverse.entity.Order;
import dev.peacechan.foodverse.entity.OrderItem;
import dev.peacechan.foodverse.order.dto.OrderItemResponse;
import dev.peacechan.foodverse.order.dto.OrderResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderItem toOrderItem(Order order, Menu menu, Integer quantity) {
        BigDecimal lineTotal = menu.getPrice().multiply(BigDecimal.valueOf(quantity.longValue()));

        return OrderItem.builder()
                .order(order)
                .menu(menu)
                .quantity(quantity)
                .unitPrice(menu.getPrice())
                .lineTotal(lineTotal)
                .build();
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getOrderedAt(),
                order.getCustomerProfile().getId(),
                order.getRestaurant().getId(),
                itemResponses
        );
    }

    private OrderItemResponse toItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getMenu().getId(),
                orderItem.getMenu().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getLineTotal()
        );
    }
}
