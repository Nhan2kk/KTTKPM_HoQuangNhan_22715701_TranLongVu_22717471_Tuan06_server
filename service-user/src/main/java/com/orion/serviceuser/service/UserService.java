package com.orion.serviceuser.service;

import com.orion.serviceuser.dto.AuthResponse;
import com.orion.serviceuser.dto.LoginRequest;
import com.orion.serviceuser.dto.RegisterRequest;
import com.orion.serviceuser.dto.UserResponse;
import com.orion.serviceuser.exception.BadRequestException;
import com.orion.serviceuser.exception.NotFoundException;
import com.orion.serviceuser.model.User;
import com.orion.serviceuser.repository.UserRepository;
import com.orion.serviceuser.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());

        UserResponse userResponse = mapToUserResponse(savedUser);
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());

        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if user is active
        if (!user.getActive()) {
            throw new BadRequestException("User account is disabled");
        }

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        log.info("User logged in successfully: {}", user.getId());

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        UserResponse userResponse = mapToUserResponse(user);
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .message("Login successful")
                .build();
    }

    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return mapToUserResponse(user);
    }

    // ===== ADMIN METHODS =====

    public UserResponse createUserByAdmin(com.orion.serviceuser.dto.CreateUserByAdminRequest request) {
        log.info("Admin creating new user: {}", request.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Validate role
        if (!request.getRole().equals("USER") && !request.getRole().equals("ADMIN")) {
            throw new BadRequestException("Role must be USER or ADMIN");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Admin created user: {}", savedUser.getId());

        return mapToUserResponse(savedUser);
    }

    public UserResponse updateUser(Long id, com.orion.serviceuser.dto.UpdateUserRequest request) {
        log.info("Admin updating user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Update email if provided and different
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        // Update password if provided
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", id);

        return mapToUserResponse(updatedUser);
    }

    public UserResponse changeUserRole(Long id, com.orion.serviceuser.dto.ChangeRoleRequest request) {
        log.info("Admin changing role for user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Validate role
        if (!request.getRole().equals("USER") && !request.getRole().equals("ADMIN")) {
            throw new BadRequestException("Role must be USER or ADMIN");
        }

        user.setRole(request.getRole());
        User updatedUser = userRepository.save(user);
        log.info("User role changed to: {}", request.getRole());

        return mapToUserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        log.info("Admin deleting user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
        log.info("User deleted: {}", id);
    }

    public UserResponse toggleUserStatus(Long id) {
        log.info("Admin toggling user status: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setActive(!user.getActive());
        User updatedUser = userRepository.save(user);
        log.info("User status changed to: {}", updatedUser.getActive());

        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
