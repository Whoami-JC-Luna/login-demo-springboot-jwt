package com.jcluna.auth_api.service.impl;

import com.jcluna.auth_api.dto.AuthResponse;
import com.jcluna.auth_api.dto.LoginRequest;
import com.jcluna.auth_api.dto.RegisterRequest;
import com.jcluna.auth_api.exception.InvalidCredentialsException;
import com.jcluna.auth_api.exception.UserAlreadyExistException;
import com.jcluna.auth_api.model.User;
import com.jcluna.auth_api.repository.UserRepository;
import com.jcluna.auth_api.security.JwtService;
import com.jcluna.auth_api.security.SecurityConfig;
import com.jcluna.auth_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @Override
    public String register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistException("No ha sido posible completar el registro");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUserName(request.getUserName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);

        return "Usuario registrado con éxito";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }
        String token = jwtService.generateToken(user.get());


        return new AuthResponse(token);
    }
}
