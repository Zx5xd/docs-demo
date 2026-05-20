package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "history", indexes = {
        @Index(name = "idx_history_doc_id", columnList = "history_doc_id")
})
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private int history_id;

    @Column(name = "history_doc_id")
    private int history_doc_id;

    @Column(name = "history_version", nullable = false)
    private int history_version;

    @Column(name = "history_snapshot", nullable = false, columnDefinition = "TEXT")
    private String history_snapshot;

    @Column(name = "history_action_type", nullable = false, columnDefinition = "TEXT")
    private String history_action_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_actor_id", nullable = false)
    private User actor;

    @Column(name = "history_created_at", nullable = false)
    private int history_created_at;
}
