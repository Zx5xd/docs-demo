package com.example.demo.dto;

import lombok.Data;

@Data
public class DocsDTO {

    private int doc_id;
    private String doc_title;
    private String doc_content;
    private int doc_createdAt;
    private Integer doc_updatedAt;
    private String doc_writer;
    private int doc_category;
    private int doc_weight;
    private String doc_type;
}
