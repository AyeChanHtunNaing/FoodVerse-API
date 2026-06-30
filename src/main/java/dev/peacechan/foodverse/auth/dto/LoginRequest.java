package dev.peacechan.foodverse.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for user login.")
public record LoginRequest(
        @Schema(description = "Registered email address.", example = "alice@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,
        @Schema(description = "Account password.", example = "SecurePass123")
        @NotBlank(message = "Password is required")
        String password
) {
}
