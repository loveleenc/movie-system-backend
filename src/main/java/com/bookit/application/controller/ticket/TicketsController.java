package com.bookit.application.controller.ticket;


import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.dto.ticket.TicketStatusUpdateDto;
import com.bookit.application.services.TicketService;
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
                .stream().map(this.ticketDTOMapper::toTicketForShowDTO).toList();
    }

    @PatchMapping("/tickets")
    String updateTicketStatusForShow(@RequestBody TicketStatusUpdateDto ticketStatusUpdateDto){
        this.ticketService.updateTicketStatusForShow(ticketStatusUpdateDto.getShowId(), ticketStatusUpdateDto.getStatus(), false);
        return "Update successful";
    }
}
