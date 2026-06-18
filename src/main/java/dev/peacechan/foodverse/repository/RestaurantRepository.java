package dev.peacechan.foodverse.repository;

import dev.peacechan.foodverse.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
