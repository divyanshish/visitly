package com.visitly.myproject.mapper;

import com.visitly.myproject.dto.RoleDto;
import com.visitly.myproject.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleDto toDto(Role role);


    Role toEntity(RoleDto roleDto);
}
