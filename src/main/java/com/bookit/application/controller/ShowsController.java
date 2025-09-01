package com.bookit.application.controller;

import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.DTO.show.ShowDTOMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowsController {
    private ShowService showService;
    private ShowDTOMapper showDTOMapper;

    ShowsController(ShowService showService, ShowDTOMapper showDTOMapper) {
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }

    @GetMapping("/shows/{movieExternalId}")
    ResponseEntity<List<ShowDTO>> getShowsByMovie(@PathVariable String movieExternalId) {
        List<ShowDTO> shows = this.showService.getShowsByMovie(movieExternalId).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    //TODO: add methods to update and delete shows

    @PostMapping("/show")
    ResponseEntity<ShowDTO> createShow(@RequestBody Show show) {
        Show createdShow = this.showService.createShow(show);
        return new ResponseEntity<>(this.showDTOMapper.toDTO(createdShow), HttpStatus.CREATED);
    }
}
