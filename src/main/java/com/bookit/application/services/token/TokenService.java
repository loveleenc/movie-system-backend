package com.bookit.application.services.token;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {
    @Value("${account.activation}")
    private String activationSecret;

    private Date accountActivationLinkExpiry(){
        return Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String createActivationToken(String username){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(activationSecret));

        return Jwts.builder()
                .subject(username)
                .expiration(accountActivationLinkExpiry())
                .signWith(key)
                .compact();
    }

}
