package dev.peacechan.foodverse.customer.repository;

import dev.peacechan.foodverse.customer.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
}
