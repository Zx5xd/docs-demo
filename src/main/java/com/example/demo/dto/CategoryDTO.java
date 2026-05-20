package com.example.demo.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private int category_id;
    private String category_name;
    private int category_createdAt;
    private Integer category_updatedAt;
}
