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
}
