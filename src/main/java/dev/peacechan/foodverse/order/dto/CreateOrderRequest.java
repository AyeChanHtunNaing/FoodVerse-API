package dev.peacechan.foodverse.order.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Request payload for placing an order.")
public record CreateOrderRequest(
        @Schema(description = "Restaurant id the order belongs to.", example = "3")
        @NotNull(message = "Restaurant id is required")
        Long restaurantId,
        @ArraySchema(schema = @Schema(implementation = CreateOrderItemRequest.class))
        @NotEmpty(message = "Order items must not be empty")
        List<@Valid CreateOrderItemRequest> items
) {
}
