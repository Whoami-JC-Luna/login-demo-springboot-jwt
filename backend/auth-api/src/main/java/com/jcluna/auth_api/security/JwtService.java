package com.jcluna.auth_api.security;


import com.jcluna.auth_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // JWT signed with HMAC-SHA256. Secret loaded from environment, never hardcoded (OWASP A04:2025 - Cryptographic Failures)

    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.expiration}")
    private long expiration;



    // Secret decoded from Base64 and used to sign/verify JWT tokens (HMAC-SHA256)
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Parses and verifies the JWT token. Single entry point to avoid duplicate parsing.
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String email) {
        try {
            return getClaims(token).getSubject().equals(email) && !isTokenExpired(token);
        }
        // Expired token is not valid. Return false instead of propagating the exception.
        catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

}
