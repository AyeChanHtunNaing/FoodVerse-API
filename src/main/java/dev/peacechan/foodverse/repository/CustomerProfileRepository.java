package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
}
