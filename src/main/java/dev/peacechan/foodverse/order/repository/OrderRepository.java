package dev.peacechan.foodverse.order.repository;

import dev.peacechan.foodverse.order.entity.Order;
import dev.peacechan.foodverse.order.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndCustomerProfileUserEmail(Long id, String email);

    List<Order> findAllByStatusAndOrderedAtBefore(OrderStatus status, LocalDateTime orderedAt);

    boolean existsByRestaurantId(Long restaurantId);
}
