package com.bookit.application.types;

public enum SeatCategory {
    GOLD("gold"),
    SILVER("silver"),
    BRONZE("bronze");

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
