package com.jcluna.auth_api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String userName;
    private String email;
    private String password;
}
