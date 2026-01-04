package com.bookit.application.security.user.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {
    private SecretKey activationSecretKey;

    public TokenService(){
        this.activationSecretKey = Jwts.SIG.HS256.key().build();
    }

    private Date accountActivationLinkExpiry(){
        return Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String createActivationToken(String username){
        return Jwts.builder()
                .subject(username)
                .expiration(accountActivationLinkExpiry())
                .signWith(activationSecretKey)
                .compact();
    }

    public String getUsernameFromActivationToken(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(activationSecretKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }



}
