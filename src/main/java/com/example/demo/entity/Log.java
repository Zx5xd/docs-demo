package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "log")
@Data
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int log_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_author", referencedColumnName = "user_login_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_doc_id", nullable = false)
    private Docs docs;

    @Column(name = "log_doc_name", nullable = false, columnDefinition = "TEXT")
    private String log_doc_name;

    @Column(name = "log_content", nullable = false, columnDefinition = "TEXT")
    private String log_content;

    @Column(name = "log_action_type", nullable = false, columnDefinition = "TEXT")
    private String log_action_type;

    @Column(name = "log_timestamp", nullable = false)
    private int log_timestamp;
}
