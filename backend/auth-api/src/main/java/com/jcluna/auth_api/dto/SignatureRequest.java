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
public class SignatureRequest {
    @NotBlank(message = "No has insertado texto")
    private String signature;
    @NotBlank(message = "Se necesita un nombre para la firma")
    private String author;
}
