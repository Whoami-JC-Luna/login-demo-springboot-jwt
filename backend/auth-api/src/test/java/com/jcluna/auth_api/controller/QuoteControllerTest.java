package com.jcluna.auth_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.exception.QuoteNotFoundException;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.security.JwtAuthFilter;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuoteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteService quoteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private QuoteResponse testQuoteResponse;
    private QuoteRequest testQuoteRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setRole(Role.ROLE_USER);

        testQuoteResponse = new QuoteResponse(UUID.randomUUID(), "Test text", "Test author");

        testQuoteRequest = new QuoteRequest();
        testQuoteRequest.setText("Test text");
        testQuoteRequest.setAuthor("Test author");
    }

    // Test 1
    @Test
    void getRandomQuote_shouldReturn200() throws Exception {
        // Given
        when(quoteService.getRandomQuote()).thenReturn(testQuoteResponse);

        // When & Then
        mockMvc.perform(get("/quotes/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Test text"))
                .andExpect(jsonPath("$.author").value("Test author"));
    }

    // Test 2
    @Test
    void addQuote_shouldReturn201_whenQuoteIsAdded() throws Exception {
        // Given
        when(quoteService.addQuote(any(QuoteRequest.class), any())).thenReturn(testQuoteResponse);

        // When & Then
        mockMvc.perform(post("/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testQuoteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Test text"));
    }

    // Test 3
    @Test
    void deleteQuote_shouldReturn204_whenQuoteIsDeleted() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(quoteService).deleteQuote(any(UUID.class), any());

        // When & Then
        mockMvc.perform(delete("/quotes/" + id))
                .andExpect(status().isNoContent());
    }


    // Test 4
    @Test
    void getRandomQuote_shouldReturn404_whenNoQuotesAvailable() throws Exception {
        // Given
        when(quoteService.getRandomQuote()).thenThrow(new QuoteNotFoundException("No se encontró el recurso"));

        // When & Then
        mockMvc.perform(get("/quotes/random"))
                .andExpect(status().isNotFound());
    }

}
