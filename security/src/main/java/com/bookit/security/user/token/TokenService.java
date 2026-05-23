package com.bookit.security.user.token;


import com.bookit.security.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService implements ITokenService<String> {
    private SecretKey activationSecretKey;
    public TokenService(){
        this.activationSecretKey = Jwts.SIG.HS256.key().build();
    }

    private Date getTokenExpiry(){

        return Date.from(
                LocalDate.now()
                        .plusDays(5)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
    }

    @Override
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .expiration(getTokenExpiry())
                .signWith(activationSecretKey)
                .compact();
    }

    @Override
    public Claims verifyToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(activationSecretKey)
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
