package com.example.server.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    public JwtUtil() {
        // Load environment variables from .env
        Dotenv dotenv = Dotenv.load();

        String secret = dotenv.get("JWT_SECRET");
        String expirationStr = dotenv.get("JWT_EXPIRATION");

        if (secret == null || expirationStr == null) {
            throw new RuntimeException("JWT_SECRET or JWT_EXPIRATION not set in .env");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = Long.parseLong(expirationStr);
    }

    public String generateToken(String id, String role, String email, String userName) {
        return Jwts.builder()
                .claim("id", id)
                .claim("role", role)
                .claim("email", email)
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
