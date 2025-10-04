package com.bookit.application.controller.theatre;

import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.show.ShowDtoMapper;
import com.bookit.application.dto.theatre.TheatreDto;
import com.bookit.application.dto.theatre.TheatreDtoMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Theatre;
import com.bookit.application.services.ShowService;
import com.bookit.application.services.TheatreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TheatreController {
    private TheatreService theatreService;
    private TheatreDtoMapper theatreDtoMapper;
    private final ShowService showService;
    private final ShowDtoMapper showDTOMapper;

    public TheatreController(TheatreService theatreService,
                             TheatreDtoMapper theatreDtoMapper,
                             ShowService showService,
                             ShowDtoMapper showDTOMapper) {
        this.theatreService = theatreService;
        this.theatreDtoMapper = theatreDtoMapper;
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }

    @GetMapping("/theatre")
    public ResponseEntity<List<TheatreDto>> getTheatres() {
        List<Theatre> theatres = this.theatreService.getTheatres();
        return new ResponseEntity<>(this.theatreDtoMapper.toTheatreDto(theatres), HttpStatus.OK);
    }

    @PostMapping("/theatre")
    public ResponseEntity<TheatreDto> createTheatre(@RequestBody TheatreDto theatreDto) throws IllegalAccessException {
        Theatre theatre = this.theatreDtoMapper.toTheatre(theatreDto);
        Theatre createdTheatre = this.theatreService.create(theatre);
        return new ResponseEntity<>(this.theatreDtoMapper.toTheatreDto(createdTheatre), HttpStatus.CREATED);
    }

    @GetMapping("/theatre/{id}/shows")
    ResponseEntity<List<ShowDto>> getShowsByTheatre(@PathVariable Integer id) {
        List<Show> shows = this.showService.getShowsByTheatre(id);
        return new ResponseEntity<>(this.showDTOMapper.showTheatreDTO(shows), HttpStatus.OK);
    }

}
