package com.visitly.myproject.controller;

import com.visitly.myproject.dto.AdminStatsResponse;
import com.visitly.myproject.entity.User;
import com.visitly.myproject.repository.AuditLogRepository;
import com.visitly.myproject.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Admin only endpoints")
public class AdminController {
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get system statistics", description = "Admin only: System statistics including user counts and login times")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
    public ResponseEntity<AdminStatsResponse> getStats() {
        log.info("GET /admin/stats - Admin stats request");

        long totalUsers = userRepository.count();
        List<User> activeUsers = userRepository.findByIsActiveTrue();

        Map<Long, LocalDateTime> lastLogins = activeUsers.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user.getLastLoginAt() != null
                                ? user.getLastLoginAt()
                                : user.getCreatedAt()
                ));

        AdminStatsResponse stats = AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers((long) activeUsers.size())
                .lastLoginTimes(lastLogins)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("Admin stats: Total users={}, Active users={}", totalUsers, activeUsers.size());
        return ResponseEntity.ok(stats);
    }

    // ✅ ADD THIS METHOD
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Admin only: List all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET /api/admin/users - Admin requesting all users");

        try {
            List<User> users = userRepository.findAll();
            log.info("Found {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            throw e;
        }
    }


    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Admin only: Get specific user")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("GET /api/admin/users/{} - Admin accessing user", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Admin only: Delete a user")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/admin/users/{} - Admin deleting user", id);
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
