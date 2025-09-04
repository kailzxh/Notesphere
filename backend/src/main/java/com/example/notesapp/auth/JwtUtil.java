package com.example.notesapp.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationMs;
    private final long jwtRefreshExpirationMs;

    public JwtUtil(@Value("${app.jwtExpirationMs}") long jwtExpirationMs,
                   @Value("${app.jwtRefreshExpirationMs}") long jwtRefreshExpirationMs) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // auto 256-bit key
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
