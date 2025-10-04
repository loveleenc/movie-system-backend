package com.bookit.application.services;

import com.bookit.application.entity.Cart;
import com.bookit.application.entity.Seat;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITicketDao;
import com.bookit.application.persistence.IUserDao;
import com.bookit.application.security.entity.User;
import com.bookit.application.types.Role;
import com.bookit.application.types.SeatCategory;
import com.bookit.application.types.TicketStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TicketService {
    private ISeatDao seatDAO;
    private PricingService pricingService;
    private ITicketDao ticketDAO;
    private IUserDao userDao;

    public TicketService(ISeatDao seatDAO, PricingService pricingService, ITicketDao ticketDAO, IUserDao userDao) {
        this.seatDAO = seatDAO;
        this.pricingService = pricingService;
        this.ticketDAO = ticketDAO;
        this.userDao = userDao;
    }

    public List<Ticket> getTicketsByShow(@NonNull String showId) throws NullPointerException {
        return this.ticketDAO.findTicketsByShow(showId);
    }

    public List<Seat> getSeatsFromTheatre(Integer theatreId) {
        return this.seatDAO.getSeatPricesByTheatre(theatreId);
    }

    private Boolean statusChangeIsValid(List<String> currentStatuses, String newStatus, Boolean override) {
        if (!TicketStatus.isTicketStatusEnum(newStatus)) {
            return false;
        }

        if (override) {
            return true;
        }
        List<TicketStatus> currentStatusEnums = currentStatuses.stream().map(currentStatus -> TicketStatus.valueOf(currentStatus.toUpperCase())).toList();
        TicketStatus newStatusEnum = TicketStatus.valueOf(newStatus.toUpperCase());


        for (TicketStatus currentStatus : currentStatusEnums) {
            if (
                    !((currentStatus.equals(TicketStatus.BLOCKED) && newStatusEnum.equals(TicketStatus.AVAILABLE))
                            || (currentStatus.equals(TicketStatus.AVAILABLE) && newStatusEnum.equals(TicketStatus.BLOCKED)))
            ) {
                return false;
            }
        }
//        if(currentStatusEnums.contains(TicketStatus.USED) || (currentStatusEnums.contains(TicketStatus.BOOKED) &&
//                (newStatusEnum.equals(TicketStatus.AVAILABLE) || newStatusEnum.equals(TicketStatus.USED))) ||
//                (currentStatusEnums.contains(TicketStatus.BLOCKED) && newStatusEnum.equals(TicketStatus.USED))
//        ){
//            return false;
//        }
        return true;
    }

    public void updateTicketStatusForShow(String showId, String newStatus, Boolean overrideStatusChangeValidation) throws ResourceNotFoundException, UnsupportedOperationException {
        List<Ticket> tickets = this.getTicketsByShow(showId);
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Tickets for the show %s are not found", showId));
        }
        List<String> currentStatus = tickets.stream().map(Ticket::getStatus).distinct().toList();
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

    public List<Ticket> bookTickets(List<String> ticketIds) throws TicketBookingException, ResourceNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        if(!user.getRoles().contains(Role.REGULAR_USER)){
            throw new AccessDeniedException(String.format("Tickets cannot be booked by users that are not of type %s", Role.REGULAR_USER.code()));
        }
        Long userId = user.getId();

        List<Ticket> tickets = new ArrayList<>();
        ticketIds.forEach(ticketId -> {
            Ticket ticket = this.ticketDAO.findById(ticketId);
            try{
                TicketStatus ticketStatus = Objects.requireNonNull(TicketStatus.getTicketStatusEnum(ticket.getStatus()));
                if(!ticketStatus.equals(TicketStatus.AVAILABLE)){
                    throw new TicketBookingException("This ticket is not available for booking");
                }
                if(!Objects.isNull(ticket.getOwnerId())){
                    throw new TicketBookingException("This ticket has already been booked by a user");
                }
                ticket.setStatus(TicketStatus.BOOKED.code());
                ticket.setOwnerId(userId);
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

    public List<Ticket> cancelTicketBookings(List<String> ticketIds){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        if(!user.getRoles().contains(Role.REGULAR_USER)){
            throw new AccessDeniedException(String.format("Tickets cannot be booked by users that are not of type %s", Role.REGULAR_USER.code()));
        }
        Long userId = user.getId();

        List<Ticket> tickets = new ArrayList<>();
        ticketIds.forEach(ticketId -> {
            try{
                Ticket ticket = this.ticketDAO.findById(ticketId);
                TicketStatus ticketStatus = Objects.requireNonNull(TicketStatus.getTicketStatusEnum(ticket.getStatus()));
                if(!ticket.getOwnerId().equals(userId)){
                    throw new TicketBookingException("Ticket cannot be cancelled by another user");
                }
                if(!ticketStatus.equals(TicketStatus.BOOKED)){
                    throw new TicketBookingException("This ticket cannot be cancelled");
                }
                ticket.setStatus(TicketStatus.AVAILABLE.code());
                ticket.setOwnerId(null);
                tickets.add(ticket);
            }
            catch(EmptyResultDataAccessException e){
                throw new TicketBookingException("The ticket was not found");
            }
        });
        this.ticketDAO.bookOrCancelTickets(tickets);
        return tickets;
    }

}
