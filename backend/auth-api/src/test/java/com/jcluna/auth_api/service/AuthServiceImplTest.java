package com.jcluna.auth_api.service;


import com.jcluna.auth_api.dto.AuthResponse;
import com.jcluna.auth_api.dto.LoginRequest;
import com.jcluna.auth_api.dto.RegisterRequest;
import com.jcluna.auth_api.exception.InvalidCredentialsException;
import com.jcluna.auth_api.exception.UserAlreadyExistException;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.repository.UserRepository;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;


    // User test
    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;



    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUserName("testuser");
        testUser.setPassword("hashedPassword");
        testUser.setRole(Role.ROLE_USER);

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setUserName("testuser");
        registerRequest.setPassword("Password1!");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("Password1!");


    }


    // TEST register
    // Test 1
    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        // Given
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

        // Then
        assertThrows(UserAlreadyExistException.class, () -> authService.register(registerRequest));
    }

    // Test 2
    @Test
    void register_shouldReturnSuccessMessage_whenEmailIsNew() {
        // Given
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1!")).thenReturn("hashedPassword");

        // When
        String result = authService.register(registerRequest);

        // Then
        assertEquals("Usuario registrado con éxito", result);
        verify(userRepository).save(any(User.class));
    }




    // TEST login
    // Test 1
    @Test
    void login_shouldThrowException_whenEmailNotFound() {
        // Given
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }


    // Test 2
    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {
        // Given
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Password1!", "hashedPassword")).thenReturn(false);

        // Then
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }


    // Test 3
    @Test
    void login_shouldReturnAuthResponse_whenCredentialsAreValid() {
        // Given
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Password1!", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn("mocked-token");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("mocked-token", response.getToken());
    }




}
