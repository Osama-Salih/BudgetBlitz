package com.budget_blitz.secuirty;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final static String tokenType = "TOKEN_TYPE";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final JwtParser jwtParser;

    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
        this.jwtParser = Jwts.parser()
                .verifyWith(this.publicKey)
                .build();
    }


    public String generateAccessToken(final String username) {
        Map<String, Object> claims = Map.of(tokenType, "ACCESS_TOKEN");
        return buildToken(username, claims, accessTokenExpiration);
    }

    public String generateRefreshToken(final String username) {
        Map<String, Object> claims = Map.of(tokenType, "REFRESH_TOKEN");
        return buildToken(username, claims, accessTokenExpiration);
    }

    private String buildToken(final String username, final Map<String, Object> claims, final long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.privateKey)
                .compact();
    }

    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(final String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(final String token) {
        try {
            return this.jwtParser
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Failed to parse JWT: {}", e.getMessage());
            // Todo throw a butter exception
            throw new RuntimeException("Invalid or malformed JWT token", e);
        }
    }

    public String refreshAccessToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);
        if (!"REFRESH_TOKEN".equals(claims.get(tokenType))) {
            throw new RuntimeException("Invalid jwt token type");
        }
        if (isTokenExpired(refreshToken)) {
            throw new RuntimeException("expired jwt token");
        }

        final String username = claims.getSubject();
        return generateAccessToken(username);
    }
}