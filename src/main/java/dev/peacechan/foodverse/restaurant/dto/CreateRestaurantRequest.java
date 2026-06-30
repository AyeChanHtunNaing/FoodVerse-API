package dev.peacechan.foodverse.restaurant.dto;

import dev.peacechan.foodverse.enums.RestaurantStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for creating a restaurant.")
public record CreateRestaurantRequest(
        @Schema(description = "Restaurant name.", example = "Saigon Bites")
        @NotBlank(message = "Restaurant name is required")
        @Size(max = 150, message = "Restaurant name must not exceed 150 characters")
        String name,
        @Schema(description = "Restaurant address.", example = "45 Le Loi, District 1")
        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,
        @Schema(description = "Restaurant phone number.", example = "+842812345678")
        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phoneNumber,
        @Schema(description = "Restaurant operating status.", example = "OPEN")
        @NotNull(message = "Restaurant status is required")
        RestaurantStatus status
) {
}
