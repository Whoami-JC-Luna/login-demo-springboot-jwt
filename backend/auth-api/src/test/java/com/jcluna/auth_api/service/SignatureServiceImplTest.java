package com.jcluna.auth_api.service;

import com.jcluna.auth_api.dto.SignatureRequest;
import com.jcluna.auth_api.dto.SignatureResponse;
import com.jcluna.auth_api.exception.SignatureAlreadyExistException;
import com.jcluna.auth_api.exception.SignatureNotFoundException;
import com.jcluna.auth_api.mapper.SignatureMapper;
import com.jcluna.auth_api.model.Signature;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.repository.SignatureRepository;
import com.jcluna.auth_api.service.impl.SignatureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignatureServiceImplTest {

    @InjectMocks
    private SignatureServiceImpl signatureService;


    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private SignatureMapper signatureMapper;

    private User testUser;
    private Signature testSignature;
    private SignatureRequest testSignatureRequest;
    private SignatureResponse testSignatureResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setRole(Role.ROLE_USER);

        testSignature = new Signature();
        testSignature.setId(UUID.randomUUID());
        testSignature.setMessage("Test message");
        testSignature.setAuthor("Test Author");
        testSignature.setUser(testUser);

        testSignatureRequest = new SignatureRequest();
        testSignatureRequest.setMessage("Test message");
        testSignatureRequest.setAuthor("Test Author");

        testSignatureResponse = new SignatureResponse(UUID.randomUUID(), "Test message", "Test Author", Instant.now(), Instant.now());

    }


    // Test Signature
    // Test 1
    @Test
    void getMySignature_shouldThrowException_whenSignatureNotFound() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // Then
        assertThrows(SignatureNotFoundException.class, () -> signatureService.getMySignature(testUser));
    }

    // Test 2
    @Test
    void getMySignature_shouldReturnSignature_whenExists() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.of(testSignature));
        when(signatureMapper.toResponse(testSignature)).thenReturn(testSignatureResponse);

        // When
        SignatureResponse result = signatureService.getMySignature(testUser);

        // Then
        assertNotNull(result);
        assertEquals("Test message", result.getMessage());
    }

    // Test 3
    @Test
    void addMySignature_shouldThrowException_whenSignatureAlreadyExists() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.of(testSignature));

        // Then
        assertThrows(SignatureAlreadyExistException.class, () -> signatureService.addMySignature(testSignatureRequest, testUser));
    }

    // Test 4
    @Test
    void addMySignature_shouldReturnSignature_whenCreatedSuccessfully() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(signatureMapper.toEntity(testSignatureRequest)).thenReturn(testSignature);
        when(signatureRepository.save(testSignature)).thenReturn(testSignature);
        when(signatureMapper.toResponse(testSignature)).thenReturn(testSignatureResponse);

        // When
        SignatureResponse result = signatureService.addMySignature(testSignatureRequest, testUser);

        // Then
        assertNotNull(result);
        verify(signatureRepository).save(testSignature);
    }

    // Test 5
    @Test
    void updateMySignature_shouldThrowException_whenSignatureNotFound() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // Then
        assertThrows(SignatureNotFoundException.class, () -> signatureService.updateMySignature(testSignatureRequest, testUser));
    }

    // Test 6
    @Test
    void updateMySignature_shouldReturnUpdatedSignature_whenExists() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.of(testSignature));
        when(signatureRepository.save(testSignature)).thenReturn(testSignature);
        when(signatureMapper.toResponse(testSignature)).thenReturn(testSignatureResponse);

        // When
        SignatureResponse result = signatureService.updateMySignature(testSignatureRequest, testUser);

        // Then
        assertNotNull(result);
        verify(signatureRepository).save(testSignature);
    }

    // Test 7
    @Test
    void deleteMySignature_shouldThrowException_whenSignatureNotFound() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // Then
        assertThrows(SignatureNotFoundException.class, () -> signatureService.deleteMySignature(testUser));
    }

    // Test 8
    @Test
    void deleteMySignature_shouldDeleteSignature_whenExists() {
        // Given
        when(signatureRepository.findByUser(testUser)).thenReturn(Optional.of(testSignature));

        // When
        signatureService.deleteMySignature(testUser);

        // Then
        verify(signatureRepository).delete(testSignature);
    }


}
