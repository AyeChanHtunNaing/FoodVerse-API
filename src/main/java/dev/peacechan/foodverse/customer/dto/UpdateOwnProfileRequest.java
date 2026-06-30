package dev.peacechan.foodverse.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for updating the authenticated customer's profile.")
public record UpdateOwnProfileRequest(
        @Schema(description = "Full name.", example = "Bob Tran")
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,
        @Schema(description = "Phone number.", example = "+84987654321")
        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phoneNumber,
        @Schema(description = "Street address.", example = "123 Nguyen Trai Street")
        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,
        @Schema(description = "City name.", example = "Ho Chi Minh City")
        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,
        @Schema(description = "Postal code.", example = "700000")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode
) {
}
