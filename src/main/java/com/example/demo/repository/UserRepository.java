package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.user_login_id = :loginId")
    Optional<User> findByUser_login_id(@Param("loginId") String userLoginId);

    @Query("SELECT u FROM User u WHERE u.user_email = :email")
    Optional<User> findByUser_email(@Param("email") String userEmail);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.user_login_id = :loginId")
    boolean existsByUser_login_id(@Param("loginId") String userLoginId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.user_email = :email")
    boolean existsByUser_email(@Param("email") String userEmail);
}
