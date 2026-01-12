package com.bookit.venue.theatre.dto;

public class TheatreRowDto {
    private String seatType;
    private Long seatPrice;
    private Integer seatCount;
    private String rowLetter;


    public TheatreRowDto(String seatType, Long seatPrice, Integer seatCount, String rowLetter) {
        this.seatType = seatType;
        this.seatPrice = seatPrice;
        this.seatCount = seatCount;
        this.rowLetter = rowLetter;
    }

    public String getSeatType() {
        return seatType;
    }

    public Long getSeatPrice() {
        return seatPrice;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public String getRowLetter() {
        return rowLetter;
    }
}
