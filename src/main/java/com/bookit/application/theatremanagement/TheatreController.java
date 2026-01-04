package com.bookit.application.theatremanagement;

import com.bookit.application.theatremanagement.dto.TheatreDto;
import com.bookit.application.theatremanagement.dto.TheatreDtoMapper;
import com.bookit.application.theatremanagement.entity.Theatre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TheatreController {
    private TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping("/theatre")
    public ResponseEntity<List<TheatreDto>> getTheatres() {
        return new ResponseEntity<>(this.theatreService.getTheatres(), HttpStatus.OK);
    }

    @PostMapping("/theatre")
    public ResponseEntity<TheatreDto> createTheatre(@RequestBody TheatreDto theatreDto) throws IllegalAccessException {
        TheatreDto createdTheatre = this.theatreService.create(theatreDto);
        return new ResponseEntity<>(createdTheatre, HttpStatus.CREATED);
    }
}
