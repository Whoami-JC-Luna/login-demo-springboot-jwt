package com.jcluna.auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteRequest {
    @NotBlank(message = "No has insertado ninguna frase ")
    private String text;
    @NotBlank(message = "Se necesita un autor")
    private String author;
}
