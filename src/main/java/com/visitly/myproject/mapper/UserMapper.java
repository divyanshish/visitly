package com.visitly.myproject.mapper;

import com.visitly.myproject.dto.RoleDto;
import com.visitly.myproject.dto.UserResponse;
import com.visitly.myproject.entity.Role;
import com.visitly.myproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleDtos")
    UserResponse userToUserResponse(User user);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "auditLogs", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User dtoToUser(Object dto);

    @Named("rolesToRoleDtos")
    default Set<RoleDto> rolesToRoleDtos(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        return roles.stream()
                .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .createdAt(role.getCreatedAt())
                        .build())
                .collect(Collectors.toSet());
    }
}
