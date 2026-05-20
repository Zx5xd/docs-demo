package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    @Column(name = "user_login_id", unique = true, columnDefinition = "TEXT")
    private String user_login_id;

    @Column(name = "user_password", nullable = false, columnDefinition = "TEXT")
    private String user_password;

    @Column(name = "user_nickname", nullable = false, columnDefinition = "TEXT")
    private String user_nickname;

    @Column(name = "user_email", unique = true, columnDefinition = "TEXT")
    private String user_email;

    @Column(name = "user_createdAt", nullable = false)
    private int user_createdAt;
}
