package com.example.demo.repository;

import com.example.demo.entity.GrantRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrantRoleRepository extends JpaRepository<GrantRole, Long> {

    @Query("SELECT g FROM GrantRole g WHERE g.user.user_id = :userId")
    List<GrantRole> findByUser_User_id(@Param("userId") long userId);
}
