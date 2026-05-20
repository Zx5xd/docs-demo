package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "docs")
@Data
public class Docs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private int doc_id;

    @Column(name = "doc_title", nullable = false, columnDefinition = "TEXT")
    private String doc_title;

    @Column(name = "doc_content", columnDefinition = "TEXT")
    private String doc_content;

    @Column(name = "doc_createdAt", nullable = false)
    private int doc_createdAt;

    @Column(name = "doc_updatedAt")
    private Integer doc_updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_writer", referencedColumnName = "user_login_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_category", nullable = false)
    private Category category;

    @Column(name = "doc_weight", nullable = false)
    private int doc_weight;

    @Column(name = "doc_type", nullable = false, columnDefinition = "TEXT")
    private String doc_type;
}
