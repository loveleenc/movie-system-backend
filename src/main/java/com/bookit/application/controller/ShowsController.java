package com.bookit.application.controller;

import com.bookit.application.DTO.showAndTickets.ShowAndTicketDTO;
import com.bookit.application.DTO.showAndTickets.show.ShowDTO;
import com.bookit.application.DTO.showAndTickets.show.ShowDTOMapper;
import com.bookit.application.DTO.showAndTickets.ticket.TicketDTOMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShowsController {
    private ShowService showService;
    private ShowDTOMapper showDTOMapper;
    private TicketDTOMapper ticketDTOMapper;

    ShowsController(ShowService showService, ShowDTOMapper showDTOMapper, TicketDTOMapper ticketDTOMapper) {
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
        this.ticketDTOMapper = ticketDTOMapper;
    }

    @GetMapping("/shows/{movieId}")
    ResponseEntity<List<ShowDTO>> getShowsByMovie(@PathVariable Long movieId) {
        List<ShowDTO> shows = this.showService.getShowsByMovie(movieId).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    //TODO: add methods to update and delete shows

    @PostMapping("/show")
    ResponseEntity<ShowDTO> createShow(@RequestBody ShowDTO showDTO) {
        Show show = this.showDTOMapper.toShow(showDTO);
//        Show show = this.showDTOMapper.toShow(showAndTicketDto.getShow());
//        Ticket ticket = this.ticketDTOMapper.toTicket(showAndTicketDto.getTicket());
        Ticket ticket = null;
        Show createdShow = this.showService.createShowAndTickets(show, ticket);
        return new ResponseEntity<>(this.showDTOMapper.toDTO(createdShow), HttpStatus.CREATED);
    }
}
