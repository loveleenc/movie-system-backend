package com.bookit.application.controller.show;

import com.bookit.application.dto.show.ShowAndTicketCreationDto;
import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.show.ShowDtoMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShowsController {
    private ShowService showService;
    private ShowDtoMapper showDTOMapper;

    ShowsController(ShowService showService, ShowDtoMapper showDTOMapper) {
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }

    @GetMapping("/shows")
    ResponseEntity<List<ShowDto>> getShowsByMovie(@RequestParam Long movieId) {
        List<ShowDto> shows = this.showService.getShowsByMovie(movieId).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    @GetMapping("/shows")
    ResponseEntity<List<ShowDto>> getShows(@RequestParam Integer theatreId){
        List<Show> shows = this.showService.getShowsByTheatre(theatreId);
        return new ResponseEntity<>(this.showDTOMapper.showTheatreDTO(shows), HttpStatus.OK);
    }

    //TODO: add methods to update shows

    @PostMapping("/show")
    ResponseEntity<ShowDto> createShow(@RequestBody ShowAndTicketCreationDto showAndTicketCreationDTO) {
        ShowDto showDTO = showAndTicketCreationDTO.getShow();
        Show show = this.showDTOMapper.toShow(showDTO);
        Show createdShow = this.showService.createShowAndTickets(show, showAndTicketCreationDTO.getMoviePrice(), showAndTicketCreationDTO.getStatus());
        return new ResponseEntity<>(this.showDTOMapper.toDTO(createdShow), HttpStatus.CREATED);
    }


    @PatchMapping("/cancelShow")
    String cancelShow(@RequestParam String showId){
        this.showService.cancelShow(showId);
        return "Show cancelled successfully";
    }
}
