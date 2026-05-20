package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int category_id;

    @Column(name = "category_name", nullable = false, columnDefinition = "TEXT")
    private String category_name;

    @Column(name = "category_createdAt", nullable = false)
    private int category_createdAt;

    @Column(name = "category_updatedAt")
    private Integer category_updatedAt;
}
