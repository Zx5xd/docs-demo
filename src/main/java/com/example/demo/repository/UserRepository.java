package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUser_login_id(String userLoginId);
    Optional<User> findByUser_email(String userEmail);

    boolean existsByUser_login_id(String userLoginId);

    boolean existsByUser_email(String userEmail);
}
