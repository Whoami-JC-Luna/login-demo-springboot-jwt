package com.jcluna.auth_api.service;

import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {


        @InjectMocks
        private JwtService jwtService;

        private User testUser;

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(jwtService, "secretKey", "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLXB1cnBvc2VzLW9ubHktMzJjaGFycw==");
            ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);

            testUser = new User();
            testUser.setEmail("test@test.com");
        }



    // TEST isTokenExpired
    // Test 1
    @Test
    void generateToken_shouldReturnNonNullToken() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertNotNull(token);
    }

    // Test 2
    @Test
    void generateToken_shouldContainCorrectEmail() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertEquals("test@test.com", jwtService.extractEmail(token));
    }


    // Test 3
    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertTrue(jwtService.validateToken(token, "test@test.com"));
    }

    // Test 4
    @Test
    void validateToken_shouldReturnFalseForWrongEmail() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertFalse(jwtService.validateToken(token, "other@test.com"));
    }



    // Test isTokenExpired
    // Test 1

    @Test
    void isTokenExpired_shouldReturnFalseForFreshToken() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertTrue(jwtService.validateToken(token, "test@test.com"));
    }

    @Test
    void isTokenExpired_shouldReturnFalseForExpiredToken() {
        // Given - token que expira en 1 milisegundo
        ReflectionTestUtils.setField(jwtService, "expiration", 1L);
        String token = jwtService.generateToken(testUser);

        // When - esperamos a que expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then
        assertFalse(jwtService.validateToken(token, "test@test.com"));
    }








}
