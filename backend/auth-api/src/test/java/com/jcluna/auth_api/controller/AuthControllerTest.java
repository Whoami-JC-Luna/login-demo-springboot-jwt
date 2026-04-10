package com.jcluna.auth_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcluna.auth_api.dto.AuthResponse;
import com.jcluna.auth_api.dto.LoginRequest;
import com.jcluna.auth_api.dto.RegisterRequest;
import com.jcluna.auth_api.exception.InvalidCredentialsException;
import com.jcluna.auth_api.exception.UserAlreadyExistException;
import com.jcluna.auth_api.security.JwtAuthFilter;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;


    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setUserName("testuser");
        registerRequest.setPassword("Password1!");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("Password1!");
    }

    // Test 1
    @Test
    void register_shouldReturn200_whenRegistrationIsSuccessful() throws Exception {
        // Given
        when(authService.register(any())).thenReturn("Usuario registrado con éxito");

        // When & Then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado con éxito"));
    }


    // Test 2
    @Test
    void register_shouldReturn409_whenEmailAlreadyExists() throws Exception {
        // Given
        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistException("No ha sido posible completar el registro"));

        // When & Then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }




    //Login
    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {
        // Given
        AuthResponse authResponse = new AuthResponse("mocked-token", "testuser", "test@test.com");
        when(authService.login(any())).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("mocked-token")));
    }

    @Test
    void login_shouldReturn401_whenCredentialsAreInvalid() throws Exception {
        // Given
        when(authService.login(any()))
                .thenThrow(new InvalidCredentialsException("Email o contraseña incorrectos"));

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }



}
