package com.example.demo.dto;

import lombok.Data;

@Data
public class LogDTO {

    private int log_id;
    private String log_author;
    private int log_doc_id;
    private String log_doc_name;
    private String log_content;
    private String log_action_type;
    private int log_timestamp;
}
