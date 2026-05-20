package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUser_id(user.getUser_id());
        dto.setUser_login_id(user.getUser_login_id());
        dto.setUser_password(user.getUser_password());
        dto.setUser_nickname(user.getUser_nickname());
        dto.setUser_email(user.getUser_email());
        dto.setUser_createdAt(user.getUser_createdAt());
        return dto;
    }

    public UserDTO toDtoWithoutPassword(User user) {
        UserDTO dto = toDto(user);
        if (dto != null) {
            dto.setUser_password(null);
        }
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUser_id(dto.getUser_id());
        user.setUser_login_id(dto.getUser_login_id());
        user.setUser_password(dto.getUser_password());
        user.setUser_nickname(dto.getUser_nickname());
        user.setUser_email(dto.getUser_email());
        user.setUser_createdAt(dto.getUser_createdAt());
        return user;
    }

    public void updateEntity(User user, UserDTO dto) {
        if (user == null || dto == null) {
            return;
        }
        user.setUser_login_id(dto.getUser_login_id());
        if (dto.getUser_password() != null) {
            user.setUser_password(dto.getUser_password());
        }
        user.setUser_nickname(dto.getUser_nickname());
        user.setUser_email(dto.getUser_email());
        user.setUser_createdAt(dto.getUser_createdAt());
    }

    public User userRef(long userId) {
        User user = new User();
        user.setUser_id(userId);
        return user;
    }

    public User userRefByLoginId(String loginId) {
        User user = new User();
        user.setUser_login_id(loginId);
        return user;
    }

    public List<UserDTO> toDtoList(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream().map(this::toDto).toList();
    }

    public List<UserDTO> toDtoListWithoutPassword(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream().map(this::toDtoWithoutPassword).toList();
    }

    public List<User> toEntityList(List<UserDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }
}
