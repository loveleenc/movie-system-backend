package com.bookit.application.entity;

public class Ticket {
    private Show show;
    private String status;
    private Long price;
    private Seat seat;
    private String id;
    private Long ownerId;

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

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }

        if(!(object instanceof Ticket)){
            return false;
        }

        Ticket comparedTicket = (Ticket)object;
        if(this.show == comparedTicket.getShow() &&
                this.getPrice().equals(comparedTicket.getPrice()) &&
            this.seat.getSeatType().equals(comparedTicket.getSeat().getSeatType())){
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
