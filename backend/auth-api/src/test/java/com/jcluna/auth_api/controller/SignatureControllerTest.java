package com.jcluna.auth_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.security.JwtAuthFilter;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.service.SignatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignatureController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SignatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SignatureService signatureService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private SignatureResponse testSignatureResponse;
    private SignatureRequest testSignatureRequest;

    @BeforeEach
    void setUp() {
        testSignatureResponse = new SignatureResponse(
                UUID.randomUUID(), "Test message", "Test Author", Instant.now(), Instant.now()
        );

        testSignatureRequest = new SignatureRequest();
        testSignatureRequest.setMessage("Test message");
        testSignatureRequest.setAuthor("Test Author");
    }


    // Test 1
    @Test
    void addSignature_shouldReturn201_whenSignatureIsAdded() throws Exception {
        // Given
        when(signatureService.addMySignature(any(SignatureRequest.class), any())).thenReturn(testSignatureResponse);

        // When & Then
        mockMvc.perform(post("/signature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignatureRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Test message"));
    }

    // Test 2
    @Test
    void getMySignature_shouldReturn200_whenSignatureExists() throws Exception {
        // Given
        when(signatureService.getMySignature(any())).thenReturn(testSignatureResponse);

        // When & Then
        mockMvc.perform(get("/signature/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test message"));
    }

    // Test 3
    @Test
    void deleteSignature_shouldReturn204_whenSignatureIsDeleted() throws Exception {
        // Given
        doNothing().when(signatureService).deleteMySignature(any());

        // When & Then
        mockMvc.perform(delete("/signature"))
                .andExpect(status().isNoContent());
    }



}
