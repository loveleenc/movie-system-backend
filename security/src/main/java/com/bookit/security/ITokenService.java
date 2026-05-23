package com.bookit.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public interface ITokenService<T> {
    String generateToken(T requiredTokenInformation);
    Claims verifyToken(String token) throws JwtException;
}
