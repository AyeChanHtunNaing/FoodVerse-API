package dev.peacechan.foodverse.customer.mapper;

import dev.peacechan.foodverse.customer.dto.CreateCustomerRequest;
import dev.peacechan.foodverse.customer.dto.CustomerResponse;
import dev.peacechan.foodverse.customer.dto.UpdateOwnProfileRequest;
import dev.peacechan.foodverse.customer.entity.CustomerProfile;
import dev.peacechan.foodverse.auth.entity.User;
import dev.peacechan.foodverse.auth.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public User toUser(CreateCustomerRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(UserRole.USER)
                .active(true)
                .build();
    }

    public CustomerProfile toCustomerProfile(CreateCustomerRequest request, User user) {
        return CustomerProfile.builder()
                .address(request.address())
                .city(request.city())
                .postalCode(request.postalCode())
                .user(user)
                .build();
    }

    public CustomerResponse toResponse(User user) {
        CustomerProfile customerProfile = user.getCustomerProfile();

        return new CustomerResponse(
                user.getId(),
                customerProfile != null ? customerProfile.getId() : null,
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getActive(),
                customerProfile != null ? customerProfile.getAddress() : null,
                customerProfile != null ? customerProfile.getCity() : null,
                customerProfile != null ? customerProfile.getPostalCode() : null
        );
    }

    public void updateUser(User user, UpdateOwnProfileRequest request) {
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
    }

    public void updateCustomerProfile(CustomerProfile customerProfile, UpdateOwnProfileRequest request) {
        customerProfile.setAddress(request.address());
        customerProfile.setCity(request.city());
        customerProfile.setPostalCode(request.postalCode());
    }
}
