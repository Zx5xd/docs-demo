package com.example.demo.dto;

import lombok.Data;

@Data
public class HistoryDTO {

    private int history_id;
    private int history_doc_id;
    private int history_version;
    private String history_snapshot;
    private String history_action_type;
    private long history_actor_id;
    private int history_created_at;
}
