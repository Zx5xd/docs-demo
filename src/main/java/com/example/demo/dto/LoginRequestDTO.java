package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "아이디는 필수입니다.")
    private String user_login_id;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String user_password;
}
