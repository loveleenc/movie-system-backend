package com.bookit.application.booking;


import com.bookit.application.booking.dto.inbound.api.ticket.TicketDto;
import com.bookit.application.booking.dto.inbound.api.ticket.TicketDtoMapper;
import com.bookit.application.booking.entity.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketsController {
    private TicketService ticketService;
    private TicketDtoMapper ticketDtoMapper;
    private CartService cartService;

    public TicketsController(TicketService ticketService, TicketDtoMapper ticketDtoMapper, CartService cartService) {
        this.ticketService = ticketService;
        this.ticketDtoMapper = ticketDtoMapper;
        this.cartService = cartService;
    }

    @GetMapping("/tickets")
    List<TicketDto> getTicketsForShow(@RequestParam String showId){
        return this.ticketService.getTicketsByShow(showId)
                .stream().map(ticket -> this.ticketDtoMapper.toTicketDto(ticket, true)).toList();
    }

    @PatchMapping("/tickets")
    String updateTicketStatusForShow(@RequestParam TicketDto ticketDto){
        this.ticketService.updateTicketStatusForShow(ticketDto.getShowDto().getId(), ticketDto.getStatus(), false);
        return "Update successful";
    }

    @PatchMapping("/tickets/book")
    ResponseEntity<List<TicketDto>> bookTickets(){
        List<Ticket> tickets = this.cartService.confirmBooking();
        List<TicketDto> ticketDtos = this.ticketDtoMapper.toTicketDto(tickets, false);
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @PatchMapping("/tickets/cancel")
    ResponseEntity<List<TicketDto>> cancelTicketBookings(@RequestBody List<String> ticketIds){
        List<Ticket> tickets = this.ticketService.cancelBookings(ticketIds);
        List<TicketDto> ticketDtos = this.ticketDtoMapper.toTicketDto(tickets, true);
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @GetMapping("/user/tickets")
    public ResponseEntity<List<TicketDto>> getTicketsForUser(){
        List<TicketDto> ticketDtos = this.ticketService.getTicketsForUser()
                .stream().map(ticket ->
                        this.ticketDtoMapper.toTicketDto(ticket, false, ticket.getShow()))
                .toList();
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }
}
