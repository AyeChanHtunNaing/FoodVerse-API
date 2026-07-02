package dev.peacechan.foodverse.menu.dto;

import dev.peacechan.foodverse.menu.enums.MenuStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Request payload for updating a restaurant menu item.")
public record UpdateMenuRequest(
        @Schema(description = "Menu item name.", example = "Margherita Pizza")
        @NotBlank(message = "Menu name is required")
        @Size(max = 150, message = "Menu name must not exceed 150 characters")
        String name,
        @Schema(description = "Menu item description.", example = "Classic pizza with tomato, mozzarella, and basil.")
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,
        @Schema(description = "Menu item price.", example = "12.50")
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
        @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimals")
        BigDecimal price,
        @Schema(description = "Menu item availability status.", example = "AVAILABLE")
        @NotNull(message = "Menu status is required")
        MenuStatus status
) {
}
