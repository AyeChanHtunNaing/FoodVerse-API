package dev.peacechan.foodverse.customer.service;

import dev.peacechan.foodverse.common.exception.ConflictException;
import dev.peacechan.foodverse.common.exception.ResourceNotFoundException;
import dev.peacechan.foodverse.customer.dto.CreateCustomerRequest;
import dev.peacechan.foodverse.customer.dto.CustomerResponse;
import dev.peacechan.foodverse.customer.dto.UpdateOwnProfileRequest;
import dev.peacechan.foodverse.customer.mapper.CustomerMapper;
import dev.peacechan.foodverse.customer.entity.CustomerProfile;
import dev.peacechan.foodverse.auth.entity.User;
import dev.peacechan.foodverse.auth.enums.UserRole;
import dev.peacechan.foodverse.auth.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }

        User user = customerMapper.toUser(request, passwordEncoder);
        CustomerProfile customerProfile = customerMapper.toCustomerProfile(request, user);
        user.setCustomerProfile(customerProfile);

        User savedUser = userRepository.save(user);
        return customerMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return userRepository.findAllByRole(UserRole.USER).stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getOwnProfile(String email) {
        return customerMapper.toResponse(getUserByEmail(email));
    }

    @Transactional
    public CustomerResponse updateOwnProfile(String email, UpdateOwnProfileRequest request) {
        User user = getUserByEmail(email);
        customerMapper.updateUser(user, request);

        CustomerProfile customerProfile = user.getCustomerProfile();
        if (customerProfile == null) {
            customerProfile = CustomerProfile.builder()
                    .user(user)
                    .build();
            user.setCustomerProfile(customerProfile);
        }
        customerMapper.updateCustomerProfile(customerProfile, request);

        User savedUser = userRepository.save(user);
        return customerMapper.toResponse(savedUser);
    }

    @Transactional
    public void deleteCustomer(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserRole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + userId));

        userRepository.delete(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailAndRole(email, UserRole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
    }
}
