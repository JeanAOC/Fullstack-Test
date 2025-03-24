package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; // Importación añadida
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    private SecretKey ACCESS_SECRET_KEY;
    private SecretKey REFRESH_SECRET_KEY;

    @PostConstruct
    public void init() {
        // Generar las claves HMAC a partir de las cadenas
        ACCESS_SECRET_KEY = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Método para generar un access token
    public String generateAccessToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // Obtener el rol
        return buildToken(userDetails.getUsername(), role, ACCESS_SECRET_KEY, 900000);
    }

    // Método para generar un refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // Obtener el rol
        return buildToken(userDetails.getUsername(), role, REFRESH_SECRET_KEY, 604800000); // 7 días
    }

    // Método para construir un token
    private String buildToken(String subject, String role, SecretKey secretKey, long expiration) {
        return Jwts.builder()
                .subject(subject)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // Método para validar un token
    public boolean validateToken(String token, UserDetails userDetails, SecretKey secretKey) {
        final String username = extractUsername(token, secretKey);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, secretKey));
    }

    // Método para validar un refresh token
    public boolean validateRefreshToken(String refreshToken) {
        return !isTokenExpired(refreshToken, REFRESH_SECRET_KEY);
    }

    // Método para extraer el nombre de usuario de un refresh token
    public String getUsernameFromRefreshToken(String refreshToken) {
        return extractUsername(refreshToken, REFRESH_SECRET_KEY);
    }

    // Método para extraer el nombre de usuario de un token
    public String extractUsername(String token, SecretKey secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    // Método para extraer un claim específico del token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    // Método para extraer todos los claims del token
    private Claims extractAllClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Método para verificar si un token ha expirado
    private boolean isTokenExpired(String token, SecretKey secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    // Método para extraer la fecha de expiración de un token
    private Date extractExpiration(String token, SecretKey secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    // Método para obtener la clave secreta de acceso
    public SecretKey getAccessSecretKey() {
        return ACCESS_SECRET_KEY;
    }

    // Método para obtener la clave secreta de refresh
    public SecretKey getRefreshSecretKey() {
        return REFRESH_SECRET_KEY;
    }
}