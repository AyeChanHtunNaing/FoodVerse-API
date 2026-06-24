package dev.peacechan.foodverse.menu.dto;

import dev.peacechan.foodverse.enums.MenuStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateMenuRequest(
        @NotBlank(message = "Menu name is required")
        @Size(max = 150, message = "Menu name must not exceed 150 characters")
        String name,
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
        @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimals")
        BigDecimal price,
        @NotNull(message = "Menu status is required")
        MenuStatus status
) {
}
