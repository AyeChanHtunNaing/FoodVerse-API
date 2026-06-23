package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndCustomerProfileUserEmail(Long id, String email);
}
