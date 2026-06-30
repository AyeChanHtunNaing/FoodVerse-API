package dev.peacechan.foodverse.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "One line item returned in an order response.")
public record OrderItemResponse(
        @Schema(description = "Order item id.", example = "100")
        Long id,
        @Schema(description = "Menu id.", example = "15")
        Long menuId,
        @Schema(description = "Menu item name.", example = "Margherita Pizza")
        String menuName,
        @Schema(description = "Ordered quantity.", example = "2")
        Integer quantity,
        @Schema(description = "Price per item.", example = "12.50")
        BigDecimal unitPrice,
        @Schema(description = "Line total.", example = "25.00")
        BigDecimal lineTotal
) {
}
