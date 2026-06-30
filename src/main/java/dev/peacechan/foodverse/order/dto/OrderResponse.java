package dev.peacechan.foodverse.order.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order details returned by the API.")
public record OrderResponse(
        @Schema(description = "Order id.", example = "21")
        Long id,
        @Schema(description = "Generated order number.", example = "ORD-20260702-0001")
        String orderNumber,
        @Schema(description = "Current order status.", example = "PENDING")
        String status,
        @Schema(description = "Total order amount.", example = "25.00")
        BigDecimal totalAmount,
        @Schema(description = "Time when the order was placed.", example = "2026-07-02T09:30:00")
        LocalDateTime orderedAt,
        @Schema(description = "Customer profile id.", example = "10")
        Long customerProfileId,
        @Schema(description = "Restaurant id.", example = "3")
        Long restaurantId,
        @ArraySchema(schema = @Schema(implementation = OrderItemResponse.class))
        List<OrderItemResponse> items
) {
}
