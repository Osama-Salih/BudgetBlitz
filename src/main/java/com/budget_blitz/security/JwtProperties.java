package com.budget_blitz.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.security.jwt")
@Component
@Getter
@Setter
public class JwtProperties {
    private String privateKey;
    private String publicKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
