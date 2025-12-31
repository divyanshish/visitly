package com.visitly.myproject.controller;
import com.visitly.myproject.dto.AssignRoleRequest;
import com.visitly.myproject.dto.CreateRoleRequest;
import com.visitly.myproject.dto.UserResponse;
import com.visitly.myproject.entity.Role;
import com.visitly.myproject.service.RoleService;
import com.visitly.myproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Management", description = "Admin role management endpoints")
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new role", description = "Admin only: Create new role for RBAC")
    @ApiResponse(responseCode = "201", description = "Role created successfully")
    @ApiResponse(responseCode = "400", description = "Role already exists")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
    public ResponseEntity<Role> createRole(
            @Valid @RequestBody CreateRoleRequest request) {
        log.info("POST /roles - Creating new role: {}", request.getName());
        Role role = roleService.createRole(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PostMapping("/{userId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign role to user", description = "Admin only: Assign role to specific user")
    @ApiResponse(responseCode = "200", description = "Role assigned successfully")
    @ApiResponse(responseCode = "404", description = "User or role not found")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
    public ResponseEntity<UserResponse> assignRoleToUser(
            @PathVariable Long userId,
            @Valid @RequestBody AssignRoleRequest request) {
        log.info("POST /roles/{}/assign - Assigning role: {}", userId, request.getRoleName());
        UserResponse response = userService.assignRoleToUser(userId, request.getRoleName());
        return ResponseEntity.ok(response);
    }
}