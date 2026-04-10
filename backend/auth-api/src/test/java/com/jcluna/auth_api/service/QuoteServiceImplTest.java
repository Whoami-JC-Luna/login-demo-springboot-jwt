package com.jcluna.auth_api.service;


import com.jcluna.auth_api.dto.QuoteRequest;
import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.exception.QuoteNotFoundException;
import com.jcluna.auth_api.mapper.QuoteMapper;
import com.jcluna.auth_api.model.Quote;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.repository.QuoteRepository;
import com.jcluna.auth_api.service.impl.QuoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuoteServiceImplTest {


    @InjectMocks
    private QuoteServiceImpl quoteService;

    @Mock
    private QuoteRepository quoteRepository;
    @Mock
    private QuoteMapper quoteMapper;


    // User test
    private User testUser;
    private Quote testQuote;
    private QuoteRequest testQuoteRequest;
    private QuoteResponse testQuoteResponse;


    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setRole(Role.ROLE_USER);

        testQuote = new Quote();
        testQuote.setId(UUID.randomUUID());
        testQuote.setText("Test quote phrase");
        testQuote.setAuthor("Test author");
        testQuote.setUser(testUser);

        testQuoteRequest = new QuoteRequest();
        testQuoteRequest.setText("Test quote phrase");
        testQuoteRequest.setAuthor("Test author");

        testQuoteResponse = new QuoteResponse(UUID.randomUUID(), "Test quote phrase", "Test Author");

    }

    // Test Quote
    // Test 1
    @Test
    void getRandomQuote_shouldThrowException_whenNoQuotesAvailable() {
        // Given
        when(quoteRepository.findAll()).thenReturn(Collections.emptyList());

        // Then
        assertThrows(QuoteNotFoundException.class, () -> quoteService.getRandomQuote());
    }

    // Test 2
    @Test
    void getRandomQuote_shouldReturnQuote_whenQuotesAvailable() {
        // Given
        when(quoteRepository.findAll()).thenReturn(List.of(testQuote));
        when(quoteMapper.toResponse(testQuote)).thenReturn(testQuoteResponse);

        // When
        QuoteResponse result = quoteService.getRandomQuote();

        // Then
        assertNotNull(result);
        assertEquals("Test quote phrase", result.getText());
    }

    // Test 3
    @Test
    void addQuote_shouldReturnQuoteResponse_whenQuoteIsAdded() {
        // Given
        when(quoteMapper.toEntity(testQuoteRequest)).thenReturn(testQuote);
        when(quoteMapper.toResponse(testQuote)).thenReturn(testQuoteResponse);

        // When
        QuoteResponse result = quoteService.addQuote(testQuoteRequest, testUser);

        // Then
        assertNotNull(result);
        assertEquals("Test quote phrase", result.getText());
        verify(quoteRepository).save(testQuote);
    }

    // Test 4
    @Test
    void updateQuote_shouldThrowException_whenQuoteNotFound() {
        // Given
        when(quoteRepository.findByIdAndUser(testQuote.getId(), testUser)).thenReturn(Optional.empty());

        // Then
        assertThrows(QuoteNotFoundException.class, () -> quoteService.updateQuote(testQuote.getId(), testQuoteRequest, testUser));
    }

    // Test 5
    @Test
    void updateQuote_shouldReturnUpdatedQuote_whenQuoteExists() {
        // Given
        when(quoteRepository.findByIdAndUser(testQuote.getId(), testUser)).thenReturn(Optional.of(testQuote));
        when(quoteMapper.toResponse(testQuote)).thenReturn(testQuoteResponse);

        // When
        QuoteResponse result = quoteService.updateQuote(testQuote.getId(), testQuoteRequest, testUser);

        // Then
        assertNotNull(result);
        verify(quoteRepository).save(testQuote);
    }

    // Test 6
    @Test
    void deleteQuote_shouldThrowException_whenQuoteNotFound() {
        // Given
        when(quoteRepository.findByIdAndUser(testQuote.getId(), testUser)).thenReturn(Optional.empty());

        // Then
        assertThrows(QuoteNotFoundException.class, () -> quoteService.deleteQuote(testQuote.getId(), testUser));
    }

    // Test 7
    @Test
    void deleteQuote_shouldDeleteQuote_whenQuoteExists() {
        // Given
        when(quoteRepository.findByIdAndUser(testQuote.getId(), testUser)).thenReturn(Optional.of(testQuote));

        // When
        quoteService.deleteQuote(testQuote.getId(), testUser);

        // Then
        verify(quoteRepository).delete(testQuote);
    }




}
