package dev.peacechan.foodverse.restaurant.dto;

public record RestaurantResponse(
        Long id,
        String name,
        String address,
        String phoneNumber,
        String status
) {
}
