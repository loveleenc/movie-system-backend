package com.bookit.application.persistence;

import com.bookit.application.entity.Ticket;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ITicketDao {
    List<Ticket> findTicketsByShow(String showId);
    void createTickets(List<Ticket> tickets);
    void updateAllTicketsStatusForShow(String showId, String status);
    void bookOrCancelTickets(List<Ticket> tickets);
    Ticket findById(String id) throws DataAccessException;
    Integer reserveOrReleaseTicket(Ticket ticket);
    List<Ticket> findTicketsByUser(Long userId);
}
