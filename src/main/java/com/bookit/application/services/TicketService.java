package com.bookit.application.services;

import com.bookit.application.entity.Seat;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITicketDao;
import com.bookit.application.types.SeatCategory;
import com.bookit.application.types.TicketStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TicketService {
    private ISeatDao seatDAO;
    private PricingService pricingService;
    private ITicketDao ticketDAO;
    private UserService userService;

    public TicketService(ISeatDao seatDAO, PricingService pricingService, ITicketDao ticketDAO, UserService userService) {
        this.seatDAO = seatDAO;
        this.pricingService = pricingService;
        this.ticketDAO = ticketDAO;
        this.userService = userService;
    }

    public List<Ticket> getTicketsByShow(@NonNull String showId) throws NullPointerException {
        return this.ticketDAO.findTicketsByShow(showId);
    }

    public List<Seat> getSeatsFromTheatre(Integer theatreId) {
        return this.seatDAO.getSeatPricesByTheatre(theatreId);
    }

    private Boolean statusChangeIsValid(List<TicketStatus> currentStatuses, String newStatus, Boolean override) {
        if (!TicketStatus.isTicketStatusEnum(newStatus)) {
            return false;
        }

        if (override) {
            return true;
        }
        TicketStatus newStatusEnum = TicketStatus.valueOf(newStatus.toUpperCase());

        for (TicketStatus currentStatus : currentStatuses) {
            if (
                    !(
                            (currentStatus.equals(TicketStatus.BLOCKED) && newStatusEnum.equals(TicketStatus.AVAILABLE))
                            || (currentStatus.equals(TicketStatus.AVAILABLE) && newStatusEnum.equals(TicketStatus.BLOCKED))
//                            || (currentStatus.equals(TicketStatus.AVAILABLE) && newStatusEnum.equals(TicketStatus.RESERVED))
//                            || (currentStatus.equals(TicketStatus.RESERVED) && newStatusEnum.equals(TicketStatus.AVAILABLE))
                    )
            ) {
                return false;
            }
        }
        return true;
    }


    public void updateTicketStatusForShow(String showId, String newStatus, Boolean overrideStatusChangeValidation) throws ResourceNotFoundException, UnsupportedOperationException {
        //TODO: if overriding status validation, initiation refund/send out notifications for users who have booked the tickets.
        List<Ticket> tickets = this.getTicketsByShow(showId);
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Tickets for the show %s are not found", showId));
        }
        List<TicketStatus> currentStatus = tickets.stream().map(Ticket::getStatus).distinct().toList();
        if (!this.statusChangeIsValid(currentStatus, newStatus, overrideStatusChangeValidation)) {
            throw new UnsupportedOperationException(String.format("Cannot change ticket status from %s to %s", currentStatus, newStatus));
        }
        this.ticketDAO.updateAllTicketsStatusForShow(showId, newStatus);
    }

    public void createTickets(Long moviePrice, Show show, String status) throws ResourceCreationException {
        try {
            TicketStatus statusEnum = TicketStatus.valueOf(status.toUpperCase());
            if (statusEnum.equals(TicketStatus.USED) || statusEnum.equals(TicketStatus.BOOKED) || statusEnum.equals(TicketStatus.CANCELLED)) {
                throw new ResourceCreationException(String.format("Cannot create tickets with the status %s", status));
            }
        } catch (IllegalArgumentException e) {
            throw new ResourceCreationException(String.format("Cannot create tickets with the status %s", status), e);
        }

        if (moviePrice <= 0) {
            throw new ResourceCreationException("Movie price for creating tickets cannot be less than or equal to 0");
        }

        List<Seat> seats = this.getSeatsFromTheatre(show.getTheatreId());
        if (seats.isEmpty()) {
            throw new ResourceCreationException("Seats missing when trying to create tickets for a show");
        }

        List<Ticket> existingTickets = this.getTicketsByShow(show.getId().toString());
        if (!existingTickets.isEmpty()) {
            throw new ResourceCreationException("Unable to create tickets as they already exist for this show");
        }

        Map<SeatCategory, Long> ticketPricePerSeatType = new HashMap<>();
        List<Ticket> tickets = new ArrayList<>();

        for (Seat seat : seats) {
            Long ticketPrice;
            if (!ticketPricePerSeatType.containsKey(seat.getSeatType())) {
                if (seat.getSeatPrice() <= 0) {
                    throw new ResourceCreationException("Seat price should be greater than 0");
                }
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

    public void releaseTicket(Ticket ticket){
        try {
            TicketStatus ticketStatus = ticket.getStatus();
            if(!ticketStatus.equals(TicketStatus.RESERVED)){
                throw new TicketBookingException("Ticket reservation cannot be removed as it is not reserved in the first place");
            }
            ticket.setStatus(TicketStatus.AVAILABLE);
            ticket.setOwnerId(null);
            //TODO: remove ticket reservation in dao
        }
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("The ticket was not found");
        }
    }

    public Ticket reserveTicket(String ticketId, Long userId){
            try{
                Ticket ticket = this.ticketDAO.findById(ticketId);
                TicketStatus ticketStatus = ticket.getStatus();
                if(!Objects.isNull(ticket.getOwnerId())){
                    throw new TicketBookingException("This ticket has already been booked/reserved by a user");
                }
                if(!ticketStatus.equals(TicketStatus.AVAILABLE)){
                    throw new TicketBookingException("This ticket is not available for booking");
                }
                ticket.setStatus(TicketStatus.RESERVED);
                ticket.setOwnerId(userId);
                //TODO: reserve ticket in dao
                return ticket;
            }
            catch(EmptyResultDataAccessException e){
                throw new ResourceNotFoundException("The ticket was not found");
            }
    }

    public List<Ticket> bookTickets(List<Ticket> tickets) throws TicketBookingException, ResourceNotFoundException{
        tickets.forEach(ticket -> {
            try{
                TicketStatus ticketStatus = ticket.getStatus();
                if(!ticketStatus.equals(TicketStatus.RESERVED)){
                    throw new TicketBookingException("This ticket has been not reserved for booking");
                }
                ticket.setStatus(TicketStatus.BOOKED);
                tickets.add(ticket);
            }
            catch (NullPointerException e) {
                throw new TicketBookingException("Retrieved tickets from data source have an invalid ticket status", e);
            }
            catch(EmptyResultDataAccessException e){
                throw new ResourceNotFoundException("The ticket was not found");
            }
        });
        this.ticketDAO.bookOrCancelTickets(tickets);
        return tickets;
    }

    public List<Ticket> cancelBookings(List<String> ticketIds){
        Long userId = this.userService.getCurrentUserId();
        List<Ticket> tickets = new ArrayList<>();
        ticketIds.forEach(ticketId -> {
            try{
                Ticket ticket = this.ticketDAO.findById(ticketId);
                if(!ticket.isWithinCancellationDeadline()){
                    throw new TicketBookingException("Ticket cannot be cancelled when less than 30 mins are left for a show to begin");
                }
                if(!ticket.getOwnerId().equals(userId)){
                    throw new TicketBookingException("Ticket cannot be cancelled by another user");
                }
                if(!ticket.getStatus().equals(TicketStatus.BOOKED)){
                    throw new TicketBookingException("This ticket cannot be cancelled");
                }
                ticket.setStatus(TicketStatus.AVAILABLE);
                ticket.setOwnerId(null);
                tickets.add(ticket);
            }
            catch(EmptyResultDataAccessException e){
                throw new ResourceNotFoundException("The ticket was not found");
            }
        });
        this.ticketDAO.bookOrCancelTickets(tickets);
        return tickets;
    }


}
