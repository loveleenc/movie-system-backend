package com.bookit.application.dto.seat;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDto {
    private Long id;
    private String seatType;
    private String seatNumber;


    public SeatDto(String seatNumber, String seatType, Long id) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSeatType() {
        return seatType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}
