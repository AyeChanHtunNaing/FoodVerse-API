package dev.peacechan.foodverse.restaurant.controller;

import dev.peacechan.foodverse.restaurant.dto.CreateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.dto.RestaurantResponse;
import dev.peacechan.foodverse.restaurant.dto.UpdateRestaurantRequest;
import dev.peacechan.foodverse.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create restaurant",
            description = "Creates a new restaurant. Admin only. The request includes restaurant details only."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "403", description = "Only admins can create restaurants")
    })
    public RestaurantResponse createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        return restaurantService.createRestaurant(request);
    }

    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update restaurant",
            description = "Updates all editable fields of an existing restaurant. Admin only."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "403", description = "Only admins can update restaurants"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponse updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {
        return restaurantService.updateRestaurant(restaurantId, request);
    }

    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete restaurant",
            description = "Deletes a restaurant permanently by id. Admin only."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Only admins can delete restaurants"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public void deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }

    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "View restaurant",
            description = "Returns one restaurant by id. Available to authenticated admins and users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant returned successfully"),
            @ApiResponse(responseCode = "403", description = "Only authenticated admins or users can view restaurants"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public RestaurantResponse getRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "View restaurant list",
            description = "Returns the full restaurant list. Available to authenticated admins and users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant list returned successfully"),
            @ApiResponse(responseCode = "403", description = "Only authenticated admins or users can view restaurant lists")
    })
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getRestaurants();
    }
}
