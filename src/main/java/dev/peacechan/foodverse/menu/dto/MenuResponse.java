package dev.peacechan.foodverse.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Menu item details returned by the API.")
public record MenuResponse(
        @Schema(description = "Menu id.", example = "15")
        Long id,
        @Schema(description = "Menu item name.", example = "Margherita Pizza")
        String name,
        @Schema(description = "Menu item description.", example = "Classic pizza with tomato, mozzarella, and basil.")
        String description,
        @Schema(description = "Menu item price.", example = "12.50")
        BigDecimal price,
        @Schema(description = "Menu item status.", example = "AVAILABLE")
        String status,
        @Schema(description = "Restaurant id that owns the menu item.", example = "3")
        Long restaurantId
) {
}
