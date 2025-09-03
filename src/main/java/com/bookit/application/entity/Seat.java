package com.bookit.application.entity;

public class Seat {
    private Long id;
    private Long seatPrice;
    private String seatType;
    private String seatNumber;

    public Seat(String seatNumber, String seatType, Long seatPrice, Long id) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.seatPrice = seatPrice;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getSeatPrice() {
        return seatPrice;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

}
