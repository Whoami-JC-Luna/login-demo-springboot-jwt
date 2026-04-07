package com.jcluna.auth_api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class QuoteResponse {
    private UUID id;
    private String text;
    private String author;

}
