package dev.peacechan.foodverse.customer.dto;

public record CustomerResponse(
        Long userId,
        Long customerProfileId,
        String fullName,
        String email,
        String phoneNumber,
        String role,
        Boolean active,
        String address,
        String city,
        String postalCode
) {
}
