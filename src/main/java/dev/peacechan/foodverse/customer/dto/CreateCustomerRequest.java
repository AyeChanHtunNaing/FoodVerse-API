package dev.peacechan.foodverse.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for creating a customer.")
public record CreateCustomerRequest(
        @Schema(description = "Full name of the customer.", example = "Bob Tran")
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,
        @Schema(description = "Customer email address.", example = "bob@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,
        @Schema(description = "Initial account password.", example = "SecurePass123")
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,
        @Schema(description = "Customer phone number.", example = "+84987654321")
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
