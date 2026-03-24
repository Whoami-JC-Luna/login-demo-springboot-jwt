package com.jcluna.auth_api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 10, message = "El nombre de usuario debe tener entre 4 y 10 caracteres")
    private String userName;


    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email no válido")
    private String email;


    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#._\\-])[A-Za-z\\d@$!%*?&#._\\-]{8,}$",
            message = "La contraseña debe tener entre 8 y 20 caracteres, una minúscula, una mayúscula, un número y un carácter especial"
    )
    private String password;
}
