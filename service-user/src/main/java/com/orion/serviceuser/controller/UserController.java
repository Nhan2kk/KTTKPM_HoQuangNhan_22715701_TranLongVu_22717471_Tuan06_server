package com.orion.serviceuser.controller;

import com.orion.serviceuser.dto.AuthResponse;
import com.orion.serviceuser.dto.ChangeRoleRequest;
import com.orion.serviceuser.dto.CreateUserByAdminRequest;
import com.orion.serviceuser.dto.LoginRequest;
import com.orion.serviceuser.dto.RegisterRequest;
import com.orion.serviceuser.dto.UpdateUserRequest;
import com.orion.serviceuser.dto.UserResponse;
import com.orion.serviceuser.annotation.RequireAdmin;
import com.orion.serviceuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register endpoint called for user: {}", request.getUsername());
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called for user: {}", request.getUsername());
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Getting all users");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.info("Getting user by username: {}", username);
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }

    // ===== ADMIN ENDPOINTS =====

    @RequireAdmin
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserResponse>> adminGetAllUsers() {
        log.info("Admin: Getting all users");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @RequireAdmin
    @PostMapping("/admin/users")
    public ResponseEntity<UserResponse> adminCreateUser(@Valid @RequestBody CreateUserByAdminRequest request) {
        log.info("Admin: Creating new user with username: {}", request.getUsername());
        UserResponse user = userService.createUserByAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @RequireAdmin
    @PutMapping("/admin/users/{id}")
    public ResponseEntity<UserResponse> adminUpdateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Admin: Updating user with ID: {}", id);
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @RequireAdmin
    @PutMapping("/admin/users/{id}/role")
    public ResponseEntity<UserResponse> adminChangeUserRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request) {
        log.info("Admin: Changing role for user ID: {} to: {}", id, request.getRole());
        UserResponse user = userService.changeUserRole(id, request);
        return ResponseEntity.ok(user);
    }

    @RequireAdmin
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<String> adminDeleteUser(@PathVariable Long id) {
        log.info("Admin: Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @RequireAdmin
    @PutMapping("/admin/users/{id}/toggle-status")
    public ResponseEntity<UserResponse> adminToggleUserStatus(@PathVariable Long id) {
        log.info("Admin: Toggling status for user ID: {}", id);
        UserResponse user = userService.toggleUserStatus(id);
        return ResponseEntity.ok(user);
    }
}
