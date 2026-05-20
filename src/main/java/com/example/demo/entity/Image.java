package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "image")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private int img_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Docs docs;

    @Column(name = "img_name", nullable = false, columnDefinition = "TEXT")
    private String img_name;

    @Column(name = "img_alt_text", columnDefinition = "TEXT")
    private String img_alt_text;

    @Column(name = "img_content_type", nullable = false, columnDefinition = "TEXT")
    private String img_content_type;

    @Lob
    @Column(name = "img_data", nullable = false, columnDefinition = "BLOB")
    private byte[] img_data;

    @Column(name = "img_createdAt", nullable = false)
    private int img_createdAt;
}
