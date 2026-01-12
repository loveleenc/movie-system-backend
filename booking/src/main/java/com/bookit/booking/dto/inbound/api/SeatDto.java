package com.bookit.booking.dto.inbound.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDto {
    private String seatType;
    private String seatNumber;


    public SeatDto(String seatNumber, String seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public String getSeatType() {
        return seatType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}
