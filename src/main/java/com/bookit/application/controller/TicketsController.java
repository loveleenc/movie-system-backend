package com.bookit.application.controller;


import com.bookit.application.DTO.ticket.TicketDTO;
import com.bookit.application.DTO.ticket.TicketDTOMapper;
import com.bookit.application.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketsController {
    private TicketService ticketService;
    private TicketDTOMapper ticketDTOMapper;

    public TicketsController(TicketService ticketService, TicketDTOMapper ticketDTOMapper) {
        this.ticketService = ticketService;
        this.ticketDTOMapper = ticketDTOMapper;
    }

    @GetMapping("/tickets")
    List<TicketDTO> getTicketsForShow(@RequestParam String showId){
        return this.ticketService.getTicketsByShow(showId)
                .stream().map(this.ticketDTOMapper::toTicketForShowDTO).toList();
    }
}
