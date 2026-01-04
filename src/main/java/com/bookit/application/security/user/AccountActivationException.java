package com.bookit.application.services.user;

public class AccountActivationException extends RuntimeException {
    public AccountActivationException(String message) {
        super(message);
    }

    public AccountActivationException(String message, Throwable cause){
        super(message, cause);
    }
}
