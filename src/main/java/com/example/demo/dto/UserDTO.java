package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDTO {

    private long user_id;
    private String user_login_id;
    private String user_password;
    private String user_nickname;
    private String user_email;
    private int user_createdAt;
}
