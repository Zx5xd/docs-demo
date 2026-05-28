package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequestDTO {

    @NotBlank(message = "refresh_token은 필수입니다.")
    private String refresh_token;
}
