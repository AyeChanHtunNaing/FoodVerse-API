package dev.peacechan.foodverse.customer.controller;

import dev.peacechan.foodverse.customer.dto.CreateCustomerRequest;
import dev.peacechan.foodverse.customer.dto.CustomerResponse;
import dev.peacechan.foodverse.customer.dto.UpdateOwnProfileRequest;
import dev.peacechan.foodverse.customer.service.CustomerService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CustomerResponse getOwnProfile(Principal principal) {
        return customerService.getOwnProfile(principal.getName());
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CustomerResponse updateOwnProfile(
            Principal principal,
            @Valid @RequestBody UpdateOwnProfileRequest request
    ) {
        return customerService.updateOwnProfile(principal.getName(), request);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long userId) {
        customerService.deleteCustomer(userId);
    }
}
