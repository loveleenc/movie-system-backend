package com.bookit.application.booking.entity;

import org.springframework.lang.NonNull;

public class Item {
    @NonNull
    private Ticket ticket;
    private Long id;

    public Item(Ticket ticket, Long id) {
        this.ticket = ticket;
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Long getId() {
        return id;
    }
}
