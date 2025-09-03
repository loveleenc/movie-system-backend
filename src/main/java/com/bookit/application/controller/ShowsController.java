package com.bookit.application.controller;

import com.bookit.application.DTO.show.ShowAndTicketCreationDTO;
import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.DTO.show.ShowDTOMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShowsController {
    private ShowService showService;
    private ShowDTOMapper showDTOMapper;

    ShowsController(ShowService showService, ShowDTOMapper showDTOMapper) {
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }

    @GetMapping("/shows/{movieId}")
    ResponseEntity<List<ShowDTO>> getShowsByMovie(@PathVariable Long movieId) {
        List<ShowDTO> shows = this.showService.getShowsByMovie(movieId).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    //TODO: add methods to update and delete shows

    @PostMapping("/show")
    ResponseEntity<ShowDTO> createShow(@RequestBody ShowAndTicketCreationDTO showAndTicketCreationDTO) {
        ShowDTO showDTO = showAndTicketCreationDTO.getShow();
        Show show = this.showDTOMapper.toShow(showDTO);
        Show createdShow = this.showService.createShowAndTickets(show, showAndTicketCreationDTO.getMoviePrice(), showAndTicketCreationDTO.getStatus());
        return new ResponseEntity<>(this.showDTOMapper.toDTO(createdShow), HttpStatus.CREATED);
    }
}
