package org.example.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class AuthenticationService {

    private final SecretKey key;

    public AuthenticationService() {
        key = Keys.hmacShaKeyFor("97e41d694a7fa2e66373a5e15a9603b12db8b4cdfdbd3296e90a15640a3b6202b475c7d7766c5e4ad5a5e3c99ed5f665ebcf3fd5984fa7a9e3e49edfe1372ec5".getBytes());
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .signWith(key)
                .compact();
    }
}
