package com.bookit.application.controller.show;

import com.bookit.application.dto.show.ShowAndTicketCreationDto;
import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.show.ShowDtoMapper;
import com.bookit.application.dto.ticket.TicketCreationDto;
import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.entity.Show;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShowsController {
    private ShowService showService;
    private ShowDtoMapper showDTOMapper;

    ShowsController(ShowService showService, ShowDtoMapper showDTOMapper) {
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }

    //TODO: add methods to update shows

    @PostMapping("/show")
    ResponseEntity<ShowDto> createShow(@RequestBody ShowAndTicketCreationDto showAndTicketCreationDTO) {
        ShowDto showDTO = showAndTicketCreationDTO.getShowDto();
        Show show = this.showDTOMapper.toShow(showDTO);
        Show createdShow = this.showService.createShowAndTickets(show, showAndTicketCreationDTO.getMoviePrice(), showAndTicketCreationDTO.getStatus());
        return new ResponseEntity<>(this.showDTOMapper.toDTO(createdShow), HttpStatus.CREATED);
    }


    @PatchMapping("/show/cancel")
    String cancelShow(@RequestParam String showId){
        this.showService.cancelShow(showId);
        return "Show cancelled successfully";
    }

    @PostMapping("/tickets")
    public ResponseEntity<ShowDto> createTicketsForShow(@RequestBody TicketCreationDto ticketCreationDto){
        Show show = this.showService.createTicketsForExistingShow(ticketCreationDto.getShowId(), ticketCreationDto.getMoviePrice(), ticketCreationDto.getStatus());
        return new ResponseEntity<>(this.showDTOMapper.toDTO(show), HttpStatus.CREATED);
    }

    @GetMapping("/movie/{id}/shows")
    ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long id){
      List<ShowDto> shows = this.showService.getShowsByMovie(id).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
      return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    @GetMapping("/theatre/{id}/shows")
    ResponseEntity<List<ShowDto>> getShowsByTheatre(@PathVariable Integer id) {
      List<Show> shows = this.showService.getShowsByTheatre(id);
      return new ResponseEntity<>(this.showDTOMapper.toShowMovieDTO(shows), HttpStatus.OK);
    }
}
