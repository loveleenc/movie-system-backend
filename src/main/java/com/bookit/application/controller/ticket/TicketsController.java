package com.bookit.application.controller.ticket;


import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.entity.Ticket;
import com.bookit.application.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketsController {
    private TicketService ticketService;
    private TicketDtoMapper ticketDTOMapper;

    public TicketsController(TicketService ticketService, TicketDtoMapper ticketDTOMapper) {
        this.ticketService = ticketService;
        this.ticketDTOMapper = ticketDTOMapper;
    }

    @GetMapping("/tickets")
    List<TicketDto> getTicketsForShow(@RequestParam String showId){
        return this.ticketService.getTicketsByShow(showId)
                .stream().map(this.ticketDTOMapper::toTicketDto).toList();
    }

    @PatchMapping("/tickets")
    String updateTicketStatusForShow(@RequestParam TicketDto ticketDto){
        this.ticketService.updateTicketStatusForShow(ticketDto.getShow().getId(), ticketDto.getStatus(), false);
        return "Update successful";
    }

    @PatchMapping("/tickets/book")
    ResponseEntity<List<TicketDto>> bookTickets(@RequestBody List<String> ids){
        List<Ticket> tickets = this.ticketService.bookTickets(ids);
        List<TicketDto> ticketDtos = this.ticketDTOMapper.toTicketDto(tickets);
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }

    @PatchMapping("/tickets/cancel")
    ResponseEntity<List<TicketDto>> cancelTickets(@RequestBody List<String> ids){
        List<Ticket> tickets = this.ticketService.cancelTicketBookings(ids);
        List<TicketDto> ticketDtos = this.ticketDTOMapper.toTicketDto(tickets);
        return new ResponseEntity<>(ticketDtos, HttpStatus.OK);
    }



}
