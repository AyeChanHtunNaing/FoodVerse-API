package dev.peacechan.foodverse.menu.dto;

import dev.peacechan.foodverse.enums.MenuStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateMenuRequest(
        @NotBlank
        @Size(max = 150)
        String name,
        @Size(max = 500)
        String description,
        @NotNull
        @DecimalMin(value = "0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal price,
        @NotNull
        MenuStatus status
) {
}
