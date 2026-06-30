package dev.peacechan.foodverse.order.dto;

import dev.peacechan.foodverse.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for updating an order status.")
public record UpdateOrderStatusRequest(
        @Schema(description = "New order status.", example = "CONFIRMED")
        @NotNull(message = "Order status is required")
        OrderStatus status
) {
}
