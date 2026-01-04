package com.bookit.application.theatremanagement.entity;


import java.util.Objects;

public class Seat {
    private Long id;
    private Long seatPrice;
    private SeatCategory seatType;
    private String seatNumber;

    public Seat(String seatNumber, String seatType, Long seatPrice, Long id) {
        this.seatNumber = seatNumber;
        this.seatPrice = seatPrice;
        this.id = id;
        this.setSeatType(seatType);
    }

    public Seat(String seatNumber, String seatType, Long seatPrice) {
        this.seatNumber = seatNumber;
        this.seatPrice = seatPrice;
        this.setSeatType(seatType);
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

    public SeatCategory getSeatType() {
        return seatType;
    }

    private void setSeatType(String seatType){
        SeatCategory category = SeatCategory.getEnum(seatType);
        if(Objects.isNull(category)){
            throw new IllegalArgumentException("Provided seat category does not exist in the list of categories");
        }
        this.seatType = category;
    }
}
