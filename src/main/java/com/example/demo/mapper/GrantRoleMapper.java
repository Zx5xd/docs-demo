package com.example.demo.mapper;

import com.example.demo.dto.GrantRoleDTO;
import com.example.demo.entity.GrantRole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrantRoleMapper {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public GrantRoleMapper(RoleMapper roleMapper, UserMapper userMapper) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    public GrantRoleDTO toDto(GrantRole grantRole) {
        if (grantRole == null) {
            return null;
        }
        GrantRoleDTO dto = new GrantRoleDTO();
        dto.setGrant_seq_id(grantRole.getGrant_seq_id());
        if (grantRole.getRole() != null) {
            dto.setRole_id(grantRole.getRole().getRole_id());
        }
        if (grantRole.getUser() != null) {
            dto.setUser_id(grantRole.getUser().getUser_id());
        }
        return dto;
    }

    public GrantRole toEntity(GrantRoleDTO dto, Role role, User user) {
        if (dto == null) {
            return null;
        }
        GrantRole grantRole = new GrantRole();
        grantRole.setGrant_seq_id(dto.getGrant_seq_id());
        grantRole.setRole(role);
        grantRole.setUser(user);
        return grantRole;
    }

    public GrantRole toEntity(GrantRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        Role role = roleMapper.roleRef(dto.getRole_id());
        User user = userMapper.userRef(dto.getUser_id());
        return toEntity(dto, role, user);
    }

    public void updateEntity(GrantRole grantRole, GrantRoleDTO dto, Role role, User user) {
        if (grantRole == null || dto == null) {
            return;
        }
        if (role != null) {
            grantRole.setRole(role);
        }
        if (user != null) {
            grantRole.setUser(user);
        }
    }

    public List<GrantRoleDTO> toDtoList(List<GrantRole> grantRoles) {
        if (grantRoles == null) {
            return List.of();
        }
        return grantRoles.stream().map(this::toDto).toList();
    }

    public List<GrantRole> toEntityList(List<GrantRoleDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
