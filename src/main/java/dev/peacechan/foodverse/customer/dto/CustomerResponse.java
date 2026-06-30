package dev.peacechan.foodverse.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer details returned by the API.")
public record CustomerResponse(
        @Schema(description = "User id.", example = "1")
        Long userId,
        @Schema(description = "Customer profile id.", example = "10")
        Long customerProfileId,
        @Schema(description = "Full name.", example = "Bob Tran")
        String fullName,
        @Schema(description = "Email address.", example = "bob@example.com")
        String email,
        @Schema(description = "Phone number.", example = "+84987654321")
        String phoneNumber,
        @Schema(description = "Assigned role.", example = "ROLE_USER")
        String role,
        @Schema(description = "Whether the account is active.", example = "true")
        Boolean active,
        @Schema(description = "Street address.", example = "123 Nguyen Trai Street")
        String address,
        @Schema(description = "City name.", example = "Ho Chi Minh City")
        String city,
        @Schema(description = "Postal code.", example = "700000")
        String postalCode
) {
}
