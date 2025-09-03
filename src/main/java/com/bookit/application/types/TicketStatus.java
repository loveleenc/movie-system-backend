package com.bookit.application.types;

public enum TicketStatus {
    AVAILABLE("available"),
    BOOKED("booked"),
    BLOCKED("blocked"),
    USED("used");

    private final String code;

    TicketStatus(String code){
        this.code = code;
    }

    public String code(){
        return this.code;
    }

    public static boolean isTicketStatusEnum(String possibleStatus){
        for(TicketStatus status: TicketStatus.values()){
            if(status.code().equals(possibleStatus)){
                return true;
            }
        }
        return false;
    }


}
