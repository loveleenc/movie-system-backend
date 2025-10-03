package com.bookit.application.dto.types;


public enum MaskedTicketStatus {
    AVAILABLE("available"),
    BOOKED("booked");

    private final String code;

    MaskedTicketStatus(String code){
        this.code = code;
    }

    public String code(){
        return this.code;
    }
}
