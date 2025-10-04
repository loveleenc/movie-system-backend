package com.bookit.application.types;

public enum SeatCategory {
    GOLD("Gold"),
    SILVER("Silver"),
    BRONZE("Bronze");

    private String code;

    SeatCategory(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static SeatCategory getEnum(String possibleStatus){
        for(SeatCategory status: SeatCategory.values()){
            if(status.code().equals(possibleStatus)){
                return status;
            }
        }
        return null;
    }
}
