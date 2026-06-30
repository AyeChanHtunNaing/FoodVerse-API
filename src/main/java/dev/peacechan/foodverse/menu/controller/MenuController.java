package dev.peacechan.foodverse.menu.controller;

import dev.peacechan.foodverse.common.payload.ApiErrorResponse;
import dev.peacechan.foodverse.menu.dto.CreateMenuRequest;
import dev.peacechan.foodverse.menu.dto.MenuResponse;
import dev.peacechan.foodverse.menu.dto.UpdateMenuRequest;
import dev.peacechan.foodverse.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/restaurants/{restaurantId}/menus")
@RequiredArgsConstructor
@Tag(name = "Menus", description = "Restaurant menu management endpoints.")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add menu", description = "Adds a menu to a restaurant. Admin only.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Menu created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admins can add menus",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public MenuResponse createMenu(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuRequest request
    ) {
        return menuService.createMenu(restaurantId, request);
    }

    @PutMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update menu", description = "Updates a menu belonging to a restaurant. Admin only.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admins can update menus",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant or menu not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public MenuResponse updateMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @Valid @RequestBody UpdateMenuRequest request
    ) {
        return menuService.updateMenu(restaurantId, menuId, request);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu", description = "Deletes a menu from a restaurant. Admin only.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Menu deleted successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admins can delete menus",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant or menu not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public void deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        menuService.deleteMenu(restaurantId, menuId);
    }

    @GetMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "View menu", description = "Returns one menu from a restaurant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Menu returned successfully"),
            @ApiResponse(
                    responseCode = "403",
                    description = "Authentication or required role missing",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant or menu not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public MenuResponse getMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return menuService.getMenu(restaurantId, menuId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "View restaurant menus", description = "Returns all menus belonging to a restaurant.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Menus returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MenuResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Authentication or required role missing",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public List<MenuResponse> getMenus(@PathVariable Long restaurantId) {
        return menuService.getMenus(restaurantId);
    }
}
