package com.bookit.application.services;

import com.bookit.application.entity.Seat;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.SeatDao;
import com.bookit.application.persistence.TicketDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TicketService {
    private SeatDao seatDAO;
    private PricingService pricingService;
    private TicketDao ticketDAO;

    public TicketService(SeatDao seatDAO, PricingService pricingService, TicketDao ticketDAO) {
        this.seatDAO = seatDAO;
        this.pricingService = pricingService;
        this.ticketDAO = ticketDAO;
    }

    public List<Ticket> getTicketsByShow(String showId) {
        return this.ticketDAO.findTicketsByShow(showId);
    }

    public List<Seat> getSeatsFromTheatre(Long theatreId) {
        return this.seatDAO.getSeatPricesByTheatre(theatreId);
    }

    public void createTicketsForShow(Long moviePrice, Show show, String status) {
        List<Seat> seats = this.getSeatsFromTheatre(show.getTheatreId());
        this.createTickets(moviePrice, show, status, seats);
    }

    private void createTickets(Long moviePrice, Show show, String status, List<Seat> seats) {
        //TODO: validate status type and movieprice
        Map<String, Long> ticketPricePerSeatType = new HashMap<>();
        List<Ticket> tickets = new ArrayList<>();

        for (Seat seat : seats) {
            Long ticketPrice;
            if (!ticketPricePerSeatType.containsKey(seat.getSeatType())) {
                ticketPrice = this.pricingService.calculateTicketPrice(seat.getSeatPrice(), moviePrice, show.getTimeSlot());
                ticketPricePerSeatType.put(seat.getSeatType(), ticketPrice);
            } else {
                ticketPrice = ticketPricePerSeatType.get(seat.getSeatType());
            }
            Ticket ticket = new Ticket(show, seat, status, ticketPrice);
            tickets.add(ticket);
        }
        this.ticketDAO.createTickets(tickets);
    }
}
