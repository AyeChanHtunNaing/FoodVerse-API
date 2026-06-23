package dev.peacechan.foodverse.order.dto;

import dev.peacechan.foodverse.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull
        OrderStatus status
) {
}
