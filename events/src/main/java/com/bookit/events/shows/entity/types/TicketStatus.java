package com.bookit.events.shows.entity.types;

import java.util.Arrays;

public enum TicketStatus {
    AVAILABLE("available"),
    BOOKED("booked"),
    BLOCKED("blocked"),
    USED("used"),
    CANCELLED("cancelled"),
    RESERVED("reserved");

    private final String code;

    TicketStatus(String code){
        this.code = code;
    }

    public String code(){
        return this.code;
    }

    public static boolean isTicketStatusEnum(String possibleStatus){
        return Arrays.stream(TicketStatus.values()).anyMatch(status -> status.code().equals(possibleStatus));
    }

    public static TicketStatus getTicketStatusEnum(String possibleStatus){
        for(TicketStatus status: TicketStatus.values()){
            if(status.code().equals(possibleStatus)){
                return status;
            }
        }
        return null;
    }

}
