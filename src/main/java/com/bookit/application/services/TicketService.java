package com.bookit.application.services;

import com.bookit.application.entity.Seat;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITicketDao;
import com.bookit.application.types.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TicketService {
    private ISeatDao seatDAO;
    private PricingService pricingService;
    private ITicketDao ticketDAO;

    public TicketService(ISeatDao seatDAO, PricingService pricingService, ITicketDao ticketDAO) {
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

    private Boolean statusChangeIsValid(String currentStatus, String newStatus, Boolean override){
        if(override){
            return true;
        }
        if(!TicketStatus.isTicketStatusEnum(newStatus)){
            return false;
        }
        TicketStatus currentStatusEnum = TicketStatus.valueOf(currentStatus.toUpperCase());
        TicketStatus newStatusEnum = TicketStatus.valueOf(newStatus.toUpperCase());

        if(currentStatusEnum.equals(TicketStatus.USED)){
            return false;
        }

        if(currentStatusEnum.equals(TicketStatus.BOOKED) &&
                (newStatusEnum.equals(TicketStatus.AVAILABLE) || newStatusEnum.equals(TicketStatus.USED))){
            return false;
        }

        return true;
    }

    public void updateTicketStatusForShow(String showId, String newStatus, Boolean overrideStatusChangeValidation){
        List<Ticket> tickets = this.getTicketsByShow(showId);
        if(tickets.isEmpty()){
            throw new ResourceNotFoundException(String.format("Tickets for the show %s are not found", showId));
        }
        String currentStatus = tickets.get(0).getStatus();
        if (!this.statusChangeIsValid(currentStatus, newStatus, overrideStatusChangeValidation)){
            throw new UnsupportedOperationException(String.format("Cannot change ticket status from %s to %s", currentStatus, newStatus));
        }
        this.ticketDAO.updateTicketStatus(showId, newStatus);
    }

    private void createTickets(Long moviePrice, Show show, String status, List<Seat> seats) {
        //TODO: validate tickets do not already exist for this show
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
