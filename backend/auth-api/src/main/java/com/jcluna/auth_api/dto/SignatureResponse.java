package com.jcluna.auth_api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class SignatureResponse {
    private UUID id;
    private String message;
    private String author;
    private Instant createdAt;
    private Instant updatedAt;

}
