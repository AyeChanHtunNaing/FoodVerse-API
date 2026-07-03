package dev.peacechan.foodverse.order.controller;

import dev.peacechan.foodverse.common.payload.ApiErrorResponse;
import dev.peacechan.foodverse.order.dto.CreateOrderRequest;
import dev.peacechan.foodverse.order.dto.OrderResponse;
import dev.peacechan.foodverse.order.dto.UpdateOrderStatusRequest;
import dev.peacechan.foodverse.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order placement and order management endpoints.")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place order", description = "Places a new order for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only users can place orders",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant or menu not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public OrderResponse placeOrder(
            @Parameter(hidden = true) Principal principal,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.placeOrder(principal.getName(), request);
    }

    @GetMapping("/{orderId:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get order", description = "Returns an order by id. Admin only.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order returned successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admins can view any order",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/me/{orderId:\\d+}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get own order", description = "Returns one order belonging to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order returned successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only users can view their own orders",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public OrderResponse getOwnOrder(@PathVariable Long orderId, @Parameter(hidden = true) Principal principal) {
        return orderService.getOwnOrder(orderId, principal.getName());
    }

    @PatchMapping("/me/{orderId:\\d+}/cancel")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Cancel own order", description = "Cancels an order belonging to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order cannot be cancelled",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only users can cancel their own orders",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public OrderResponse cancelOwnOrder(@PathVariable Long orderId, @Parameter(hidden = true) Principal principal) {
        return orderService.cancelOwnOrder(orderId, principal.getName());
    }

    @PatchMapping("/{orderId:\\d+}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Updates the status of an existing order. Admin only.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admins can update order status",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public OrderResponse updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(orderId, request);
    }
}
