package com.jcluna.auth_api.controller;


import com.jcluna.auth_api.dto.AuthResponse;
import com.jcluna.auth_api.dto.LoginRequest;
import com.jcluna.auth_api.dto.RegisterRequest;
import com.jcluna.auth_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
      return authService.register(request);
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }


}
