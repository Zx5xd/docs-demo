package com.example.demo.dto;

import lombok.Data;

@Data
public class ForgotPasswordResponseDTO {

    private String message;
    private String temp_password;
}
