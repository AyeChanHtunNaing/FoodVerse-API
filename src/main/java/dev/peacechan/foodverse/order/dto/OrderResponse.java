package dev.peacechan.foodverse.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String status,
        BigDecimal totalAmount,
        LocalDateTime orderedAt,
        Long customerProfileId,
        Long restaurantId,
        List<OrderItemResponse> items
) {
}
