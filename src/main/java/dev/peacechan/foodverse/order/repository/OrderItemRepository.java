package dev.peacechan.foodverse.order.repository;

import dev.peacechan.foodverse.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByMenuId(Long menuId);
}
