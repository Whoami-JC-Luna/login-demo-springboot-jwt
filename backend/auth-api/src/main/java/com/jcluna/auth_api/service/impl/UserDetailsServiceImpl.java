package com.jcluna.auth_api.service.impl;


import com.jcluna.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Constructor injection preferred over @Autowired: explicit dependencies, immutability and easier testing.
    // Lombok @RequiredArgsConstructor generates the constructor automatically for all final fields.
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado:"));
    }
}
