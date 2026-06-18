package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
