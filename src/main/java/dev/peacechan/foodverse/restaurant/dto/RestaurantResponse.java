package dev.peacechan.foodverse.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Restaurant details returned by the API.")
public record RestaurantResponse(
        @Schema(description = "Restaurant id.", example = "3")
        Long id,
        @Schema(description = "Restaurant name.", example = "Saigon Bites")
        String name,
        @Schema(description = "Restaurant address.", example = "45 Le Loi, District 1")
        String address,
        @Schema(description = "Restaurant phone number.", example = "+842812345678")
        String phoneNumber,
        @Schema(description = "Restaurant status.", example = "OPEN")
        String status
) {
}
