package dev.peacechan.foodverse.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long menuId,
        String menuName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
