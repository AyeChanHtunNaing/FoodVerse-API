package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByMenuId(Long menuId);
}
