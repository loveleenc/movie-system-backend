package com.bookit.security;

public class UsernameOrEmailAlreadyExistsException extends RuntimeException {
    public UsernameOrEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameOrEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
