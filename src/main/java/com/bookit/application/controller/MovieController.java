package com.bookit.application.controller;


import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;

    MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/movie/{id}")
    MovieDTO getMovie(@RequestParam Long id){
        return this.movieService.findMovie(id);
    }

    @GetMapping("/movies")
    List<MovieDTO> getAllMovies(){
        return this.movieService.getMovies();
    }
}
