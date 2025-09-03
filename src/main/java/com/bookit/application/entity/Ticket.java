package com.bookit.application.entity;

public class Ticket {
    private Show show;
    private String status;
    private Long price;
    private Seat seat;

    public Long getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Seat getSeat() {
        return seat;
    }

    public Show getShow() {
        return this.show;
    }

    public Ticket(Show show, Seat seat, String status, Long price) {
        this.show = show;
        this.seat = seat;
        this.status = status;
        this.price = price;
    }
}
