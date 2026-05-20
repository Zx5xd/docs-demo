package com.example.demo.dto;

import lombok.Data;

@Data
public class ImageDTO {

    private int img_id;
    private int doc_id;
    private String img_name;
    private String img_alt_text;
    private String img_content_type;
    private byte[] img_data;
    private int img_createdAt;
}
