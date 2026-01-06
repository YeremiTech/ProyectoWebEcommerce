package com.ecommerce.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final byte[] key;
    private final long ttlMs;

    public JwtService(
            @Value("${security.jwt.secret:clave-super-secreta-para-demo-256bits-1234567890}") String secret,
            @Value("${security.jwt.ttl-hours:6}") long ttlHours
    ) {
        this.key = secret.getBytes(StandardCharsets.UTF_8);
        this.ttlMs = ttlHours * 60 * 60 * 1000;
    }

    public String generate(String username, String authority) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(username)
                .claim("role", authority) 
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlMs))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key))
                .build()
                .parseClaimsJws(token);
    }
}
