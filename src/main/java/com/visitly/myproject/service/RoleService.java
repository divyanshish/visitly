package com.visitly.myproject.service;

import com.visitly.myproject.entity.Role;
import com.visitly.myproject.exception.DuplicateRoleException;
import com.visitly.myproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createRole(String name, String description) {
        log.info("Creating new role: {}", name);

        if (roleRepository.existsByName(name)) {
            log.warn("Role creation failed: Role already exists - {}", name);
            throw new DuplicateRoleException("Role already exists: " + name);
        }

        Role role = Role.builder()
                .name(name.toUpperCase())
                .description(description)
                .build();

        return roleRepository.save(role);
    }
}