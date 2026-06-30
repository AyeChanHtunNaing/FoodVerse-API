package dev.peacechan.foodverse.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing a JWT token and user details.")
public record AuthResponse(
        @Schema(description = "JWT access token.", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Authentication scheme.", example = "Bearer")
        String tokenType,
        @Schema(description = "Authenticated user id.", example = "1")
        Long userId,
        @Schema(description = "Authenticated user email.", example = "alice@example.com")
        String email,
        @Schema(description = "Authenticated user role.", example = "ROLE_USER")
        String role
) {
}
