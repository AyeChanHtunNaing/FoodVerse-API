package dev.peacechan.foodverse.order.controller;

import dev.peacechan.foodverse.order.dto.CreateOrderRequest;
import dev.peacechan.foodverse.order.dto.OrderResponse;
import dev.peacechan.foodverse.order.dto.UpdateOrderStatusRequest;
import dev.peacechan.foodverse.order.service.OrderService;
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
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(Principal principal, @Valid @RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(principal.getName(), request);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/me/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public OrderResponse getOwnOrder(@PathVariable Long orderId, Principal principal) {
        return orderService.getOwnOrder(orderId, principal.getName());
    }

    @PatchMapping("/me/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public OrderResponse cancelOwnOrder(@PathVariable Long orderId, Principal principal) {
        return orderService.cancelOwnOrder(orderId, principal.getName());
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(orderId, request);
    }
}
