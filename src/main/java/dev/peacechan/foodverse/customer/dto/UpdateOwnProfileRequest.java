package dev.peacechan.foodverse.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateOwnProfileRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,
        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phoneNumber,
        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,
        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode
) {
}
