package com.bookit.application.controller.theatre;

import com.bookit.application.dto.theatre.TheatreDto;
import com.bookit.application.dto.theatre.TheatreDtoMapper;
import com.bookit.application.entity.Theatre;
import com.bookit.application.services.TheatreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TheatreController {
    private TheatreService theatreService;
    private TheatreDtoMapper theatreDtoMapper;

    public TheatreController(TheatreService theatreService, TheatreDtoMapper theatreDtoMapper) {
        this.theatreService = theatreService;
        this.theatreDtoMapper = theatreDtoMapper;
    }

    @GetMapping("/theatre")
    public ResponseEntity<List<TheatreDto>> getTheatres(){
        List<Theatre> theatres = this.theatreService.getTheatres();
        return new ResponseEntity<>(this.theatreDtoMapper.toTheatreDto(theatres), HttpStatus.OK);
    }

    @PostMapping("/theatre")
    public  ResponseEntity<TheatreDto> createTheatre(@RequestBody TheatreDto theatreDto) throws IllegalAccessException {
        Theatre theatre = this.theatreDtoMapper.toTheatre(theatreDto);
        Theatre createdTheatre = this.theatreService.create(theatre);
        return new ResponseEntity<>(this.theatreDtoMapper.toTheatreDto(createdTheatre), HttpStatus.CREATED);
    }

}
