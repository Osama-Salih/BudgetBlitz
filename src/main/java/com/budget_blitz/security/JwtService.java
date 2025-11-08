package com.budget_blitz.security;

import com.budget_blitz.exception.BusinessException;
import com.budget_blitz.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final static String tokenType = "TOKEN_TYPE";
    private final JwtProperties jwtProperties;
    private JwtParser jwtParser;

    private PublicKey rsaPublicKey;
    private PrivateKey rsaPrivateKey;


    @PostConstruct
    private void init() {
        rsaPublicKey = getPublicKey(jwtProperties.getPublicKey());
        rsaPrivateKey = getPrivateKey(jwtProperties.getPrivateKey());
        jwtParser = Jwts.parser().verifyWith(rsaPublicKey).build();
    }

    public String generateAccessToken(final String username) {
        Map<String, Object> claims = Map.of(tokenType, "ACCESS_TOKEN");
        return buildToken(username, claims, jwtProperties.getAccessTokenExpiration());
    }

    public String generateRefreshToken(final String username) {
        Map<String, Object> claims = Map.of(tokenType, "REFRESH_TOKEN");
        return buildToken(username, claims, jwtProperties.getRefreshTokenExpiration());
    }

    private String buildToken(final String username, final Map<String, Object> claims, final long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(rsaPrivateKey)
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
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public String refreshAccessToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);
        if (!"REFRESH_TOKEN".equals(claims.get(tokenType))) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN_TYPE);
        }
        if (isTokenExpired(refreshToken)) {
            throw new BusinessException(ErrorCode.EXPIRED_JWT_TOKEN);
        }

        final String username = claims.getSubject();
        return generateAccessToken(username);
    }

    private PublicKey getPublicKey(final String pemContent) {
        if (pemContent == null || pemContent.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PUBLIC_KEY);
        }
        try {

            final String key = pemContent.replaceAll("\\s+", "");
            final byte[] keyByte = Base64.getDecoder().decode(key);
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyByte);
            return KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERR);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_PUBLIC_KEY);
        }
    }

    private PrivateKey getPrivateKey(final String pemContent) {
        if (pemContent == null || pemContent.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PRIVATE_KEY);
        }
        try {

            final String key = pemContent.replaceAll("\\s+", "");
            final byte[] keyByte = Base64.getDecoder().decode(key);
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyByte);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERR);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_PUBLIC_KEY);
        }
    }
}