package com.bookit.application.entity;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

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
