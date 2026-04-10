package com.jcluna.auth_api.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class JwtFilterSecurityTest {

    @Autowired
    private MockMvc mockMvc;


    // Method to generate expired tokens
    private String generateExpiredToken() {
        return Jwts.builder()
                .subject("test@test.com")
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLXB1cnBvc2VzLW9ubHktMzJjaGFycw==")))
                .compact();
    }



    // Test 1
    @Test
    void request_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/quotes/me"))
                .andExpect(status().isUnauthorized());
    }


    // Test 2
    @Test
    void request_withMalformedToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/quotes/me")
                        .header("Authorization", "Bearer tokenbasura"))
                .andExpect(status().isUnauthorized());
    }

    // Test 3
    @Test
    void request_withExpiredToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/quotes/me")
                        .header("Authorization", "Bearer " + generateExpiredToken()))
                .andExpect(status().isUnauthorized());
    }
}
