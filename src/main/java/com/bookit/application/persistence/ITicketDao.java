package com.bookit.application.persistence;

import com.bookit.application.entity.Ticket;

import java.util.List;

public interface ITicketDao {
    List<Ticket> findTicketsByShow(String showId);
    void createTickets(List<Ticket> tickets);
    void updateTicketStatus(String showId, String status);
}
