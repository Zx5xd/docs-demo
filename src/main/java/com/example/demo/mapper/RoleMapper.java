package com.example.demo.mapper;

import com.example.demo.dto.RoleDTO;
import com.example.demo.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleMapper {

    public RoleDTO toDto(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO dto = new RoleDTO();
        dto.setRole_id(role.getRole_id());
        dto.setRole_type(role.getRole_type());
        return dto;
    }

    public Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }
        Role role = new Role();
        role.setRole_id(dto.getRole_id());
        role.setRole_type(dto.getRole_type());
        return role;
    }

    public void updateEntity(Role role, RoleDTO dto) {
        if (role == null || dto == null) {
            return;
        }
        role.setRole_type(dto.getRole_type());
    }

    public Role roleRef(int roleId) {
        Role role = new Role();
        role.setRole_id(roleId);
        return role;
    }

    public List<RoleDTO> toDtoList(List<Role> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream().map(this::toDto).toList();
    }

    public List<Role> toEntityList(List<RoleDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
