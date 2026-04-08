package com.jcluna.auth_api.service.impl;

import com.jcluna.auth_api.dto.AuthResponse;
import com.jcluna.auth_api.dto.LoginRequest;
import com.jcluna.auth_api.dto.RegisterRequest;
import com.jcluna.auth_api.exception.InvalidCredentialsException;
import com.jcluna.auth_api.exception.UserAlreadyExistException;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.model.enums.Role;
import com.jcluna.auth_api.repository.UserRepository;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    // Constructor injection preferred over @Autowired: explicit dependencies, immutability and easier testing.
    // Lombok @RequiredArgsConstructor generates the constructor automatically for all final fields.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public String register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        // Generic error message to prevent user enumeration (OWASP A07:2025 - Authentication Failures)
        if (user.isPresent()) {
            throw new UserAlreadyExistException("No ha sido posible completar el registro");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUserName(request.getUserName());

        // Password hashed with BCrypt before persistence (OWASP A04:2025 - Cryptographic Failures)
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));


        // Role assigned server-side only. Never trust client input for role assignment (OWASP A01:2025 - Broken Access Control)
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);

        return "Usuario registrado con éxito";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        // Generic error message to prevent user enumeration (OWASP A07:2025 - Authentication Failures)
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        // Extract authenticated user to avoid multiple get() calls and improve readability
        User authenticatedUser = user.get();
        String token = jwtService.generateToken(authenticatedUser);


        return new AuthResponse(token, authenticatedUser.getUsername(), authenticatedUser.getEmail());
    }
}
