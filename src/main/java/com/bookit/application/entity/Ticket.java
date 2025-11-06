package com.bookit.application.entity;

import com.bookit.application.types.TicketStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private static final Integer TICKET_CANCELLATION_DEADLINE_OFFSET_MINUTES = 30;
    private Show show;
    private TicketStatus status;
    private Long price;
    private Seat seat;
    private String id;
    private Long ownerId;

    public Long getPrice() {
        return price;
    }

    public TicketStatus getStatus() {
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
        this.setStatus(status);
        this.price = price;
    }

    public Ticket(Show show, Seat seat, TicketStatus status, Long price) {
        this.show = show;
        this.seat = seat;
        this.status = status;
        this.price = price;
    }

    public void setStatus(String status) {
        TicketStatus statusEnum = TicketStatus.getTicketStatusEnum(status);
        if(Objects.isNull(statusEnum)){
            throw new IllegalArgumentException("Invalid status set");
        }
        this.status = statusEnum;
    }

    public void setStatus(TicketStatus status){
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

    public Boolean hasNoOwner(){
        return ownerId == 0;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean isWithinCancellationDeadline(){
        return LocalDateTime.now().isBefore(this.show.getStartTime().minusMinutes(TICKET_CANCELLATION_DEADLINE_OFFSET_MINUTES));
    }
}
