package com.bookit.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class AuthTokenProperties {
    private String secret;
    private Integer validityInMinutes;
    private String issuer;
    private Set<String> audience;

    public AuthTokenProperties() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(this.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Integer getValidityInMinutes() {
        return validityInMinutes;
    }

    public void setValidityInMinutes(Integer validityInMinutes) {
        this.validityInMinutes = validityInMinutes;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Set<String> getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = Set.of(audience.split(","));
    }
}
