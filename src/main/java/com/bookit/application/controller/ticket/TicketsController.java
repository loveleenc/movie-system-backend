package com.bookit.application.controller.ticket;


import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.entity.Ticket;
import com.bookit.application.services.CartService;
import com.bookit.application.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
                .stream().map(ticket -> this.ticketDtoMapper.toTicketDto(ticket, false)).toList();
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
    ResponseEntity<List<TicketDto>> cancelTickets(@RequestBody List<String> ticketIds){
        List<Ticket> tickets = this.ticketService.cancelBookings(ticketIds);
        List<TicketDto> ticketDtos = this.ticketDtoMapper.toTicketDto(tickets, true);
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }



}
