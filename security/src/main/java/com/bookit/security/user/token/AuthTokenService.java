package com.bookit.security.user.token;

import com.bookit.security.AuthTokenProperties;
import com.bookit.security.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class AuthTokenService implements ITokenService<Long> {

    private AuthTokenProperties authTokenProperties;

    public AuthTokenService(AuthTokenProperties authTokenProperties) {
        this.authTokenProperties = authTokenProperties;
    }

    private Date getTokenExpiry() {
        return Date.from(Instant.now()
                .plus(authTokenProperties.getValidityInMinutes(), ChronoUnit.MINUTES));
    }


    @Override
    public String generateToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .expiration(getTokenExpiry())
                .issuer(authTokenProperties.getIssuer())
                .signWith(authTokenProperties.getSecretKey())
                .compact();
    }

    @Override
    public Claims verifyToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(authTokenProperties.getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
