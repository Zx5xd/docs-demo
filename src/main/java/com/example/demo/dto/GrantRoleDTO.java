package com.example.demo.dto;

import lombok.Data;

@Data
public class GrantRoleDTO {

    private long grant_seq_id;
    private int role_id;
    private long user_id;
}
