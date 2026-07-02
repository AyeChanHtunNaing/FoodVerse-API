package dev.peacechan.foodverse.restaurant.repository;

import dev.peacechan.foodverse.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
