package com.pro.mkhub_backend.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtUtil {

    @Value("${jwt.token.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.token.refresh.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.token.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.token.refresh.expiration}")
    private long refreshTokenExpiration;

    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long id, String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(accessSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(Long id, String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(refreshSecretKey, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(accessSecretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(refreshSecretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims getClaimsFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(accessSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims getClaimsFromRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(refreshSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).getSubject();
    }

    public String getRoleFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).get("role", String.class);
    }

    public String getRoleFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).get("role", String.class);
    }

    public Long getIdFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).get("id", Long.class);
    }

    public Long getIdFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).get("id", Long.class);
    }

    public Date getExpirationDateFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).getExpiration();
    }

    public Date getExpirationDateFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).getExpiration();
    }

}