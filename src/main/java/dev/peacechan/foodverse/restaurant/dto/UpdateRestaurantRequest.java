package dev.peacechan.foodverse.restaurant.dto;

import dev.peacechan.foodverse.enums.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRestaurantRequest(
        @NotBlank
        @Size(max = 150)
        String name,
        @NotBlank
        @Size(max = 255)
        String address,
        @NotBlank
        @Size(max = 20)
        String phoneNumber,
        @NotNull
        RestaurantStatus status
) {
}
