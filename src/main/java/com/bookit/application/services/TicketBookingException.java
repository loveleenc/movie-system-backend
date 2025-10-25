package com.bookit.application.services;

public class TicketBookingException extends RuntimeException {
    private String externalMessage;

    public TicketBookingException(String message) {
        super(message);
    }

    public TicketBookingException(String message, Throwable cause){
        super(message, cause);
    }


    public TicketBookingException(String message, String externalMessage) {
        super(message);
        this.externalMessage = externalMessage;
    }

    public TicketBookingException(String message, Throwable cause, String externalMessage){
        super(message, cause);
        this.externalMessage = externalMessage;
    }
}
