package dev.peacechan.foodverse.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "One menu item inside an order request.")
public record CreateOrderItemRequest(
        @Schema(description = "Menu item id.", example = "15")
        @NotNull(message = "Menu id is required")
        Long menuId,
        @Schema(description = "Requested quantity.", example = "2")
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
