package com.example.demo.repository;

import com.example.demo.entity.GrantRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrantRoleRepository extends JpaRepository<GrantRole, Long> {

    List<GrantRole> findByUser_User_id(long userId);
}
