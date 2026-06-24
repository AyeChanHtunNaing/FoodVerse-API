package dev.peacechan.foodverse.auth.service;

import dev.peacechan.foodverse.auth.dto.AuthResponse;
import dev.peacechan.foodverse.auth.dto.LoginRequest;
import dev.peacechan.foodverse.auth.dto.RegisterRequest;
import dev.peacechan.foodverse.common.exception.ConflictException;
import dev.peacechan.foodverse.entity.User;
import dev.peacechan.foodverse.enums.UserRole;
import dev.peacechan.foodverse.repository.UserRepository;
import dev.peacechan.foodverse.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(UserRole.USER)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return new AuthResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
