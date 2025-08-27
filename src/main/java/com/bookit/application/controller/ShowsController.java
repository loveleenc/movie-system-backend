package com.bookit.application.controller;

import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.entity.Show;
import com.bookit.application.services.ShowService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowsController {
    private ShowService showService;
    ShowsController(ShowService showService){
        this.showService = showService;
    }

    @GetMapping("/shows/{movieName}")
    List<ShowDTO> getShowsByMovie(@PathVariable String movieName, @RequestParam String releaseDate){
        //TODO: handle parsing exception
        return this.showService.getShowsByMovie(movieName, LocalDate.parse(releaseDate));
    }

    //TODO: add methods to update and delete shows

    @PostMapping("/show")
    ShowDTO createShow(@RequestBody Show show){
        return this.showService.createShow(show);
    }
}
