package dev.peacechan.foodverse.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerRequest(
        @NotBlank
        @Size(max = 100)
        String fullName,
        @NotBlank
        @Email
        @Size(max = 150)
        String email,
        @NotBlank
        @Size(min = 8, max = 255)
        String password,
        @NotBlank
        @Size(max = 20)
        String phoneNumber,
        @NotBlank
        @Size(max = 255)
        String address,
        @Size(max = 50)
        String city,
        @Size(max = 20)
        String postalCode
) {
}
