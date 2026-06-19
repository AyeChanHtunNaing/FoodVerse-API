package dev.peacechan.foodverse.restaurant.mapper;

import dev.peacechan.foodverse.entity.Restaurant;
import dev.peacechan.foodverse.restaurant.dto.CreateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.dto.RestaurantResponse;
import dev.peacechan.foodverse.restaurant.dto.UpdateRestaurantRequest;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(CreateRestaurantRequest request) {
        return Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .status(request.status())
                .build();
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhoneNumber(),
                restaurant.getStatus().name()
        );
    }

    public void updateEntity(Restaurant restaurant, UpdateRestaurantRequest request) {
        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setPhoneNumber(request.phoneNumber());
        restaurant.setStatus(request.status());
    }
}
