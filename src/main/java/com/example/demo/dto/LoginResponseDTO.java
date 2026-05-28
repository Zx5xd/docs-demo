package com.example.demo.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private long user_id;
    private String user_login_id;
    private String user_nickname;
    private String user_email;
    private String access_token;
    private String refresh_token;
    private String token_type;
    private long access_expires_in;
    private long refresh_expires_in;
    private String message;
}
