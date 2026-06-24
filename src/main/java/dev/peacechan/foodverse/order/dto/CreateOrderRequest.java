package dev.peacechan.foodverse.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Restaurant id is required")
        Long restaurantId,
        @NotEmpty(message = "Order items must not be empty")
        List<@Valid CreateOrderItemRequest> items
) {
}
