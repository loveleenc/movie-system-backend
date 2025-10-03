package com.bookit.application.services;

public class TicketBookingException extends RuntimeException {
    public TicketBookingException(String message) {
        super(message);
    }

    public TicketBookingException(String message, Throwable cause){
        super(message, cause);
    }
}
