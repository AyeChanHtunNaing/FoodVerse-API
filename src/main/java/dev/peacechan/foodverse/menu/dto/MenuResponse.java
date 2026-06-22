package dev.peacechan.foodverse.menu.dto;

import java.math.BigDecimal;

public record MenuResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String status,
        Long restaurantId
) {
}
