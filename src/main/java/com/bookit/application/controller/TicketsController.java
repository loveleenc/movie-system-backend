package com.bookit.application.controller;


import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.services.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                .stream().map(this.ticketDTOMapper::toTicketForShowDTO).toList();
    }
}
