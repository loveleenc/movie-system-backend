package com.bookit.application.dto.seat;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeatDto {
    private Long id;
    private Long seatPrice;
    private String seatType;
    private String seatNumber;


    public SeatDto(String seatNumber, String seatType, Long seatPrice, Long id) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.seatPrice = seatPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getSeatPrice() {
        return seatPrice;
    }

    public String getSeatType() {
        return seatType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}
