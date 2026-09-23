package com.jcluna.auth_api.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email es obligatorio")
    private String email;
    @NotBlank(message = "Password es obligatorio")
    private String password;
}
