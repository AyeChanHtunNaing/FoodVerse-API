package dev.peacechan.foodverse.restaurant.dto;

import dev.peacechan.foodverse.enums.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRestaurantRequest(
        @NotBlank(message = "Restaurant name is required")
        @Size(max = 150, message = "Restaurant name must not exceed 150 characters")
        String name,
        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,
        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phoneNumber,
        @NotNull(message = "Restaurant status is required")
        RestaurantStatus status
) {
}
