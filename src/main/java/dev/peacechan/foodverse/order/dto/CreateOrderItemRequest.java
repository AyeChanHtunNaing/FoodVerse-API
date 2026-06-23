package dev.peacechan.foodverse.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull
        Long menuId,
        @NotNull
        @Min(1)
        Integer quantity
) {
}
