package com.bookit.application.controller;


import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.services.MovieService;
import com.bookit.application.types.MovieGenre;
import com.bookit.application.types.MovieLanguage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;

    MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    List<MovieDTO> getAllMovies(){
        return this.movieService.getMovies();
    }

    @GetMapping("/movies/ongoing")
    List<MovieDTO> getOngoingMovies(){
        return this.movieService.getOngoingMovies();
    }

    @GetMapping("/movies/upcoming")
    List<MovieDTO> getUpcomingMovies(){
        return this.movieService.getUpcomingMovies();
    }

    @GetMapping("/movies/filter")
    List<MovieDTO> filterMovies(@RequestParam(required = false) List<String> genre,
                                @RequestParam(required = false) List<String> language,
                                @RequestParam(required = false) String releasedOnOrAfter){
        if(genre == null){
            genre = new ArrayList<>();
        }
        if(language == null){
            language = new ArrayList<>();
        }
        if(releasedOnOrAfter == null){
            releasedOnOrAfter = "1970-01-01";
        }
        return this.movieService.filterMovies(genre, language, releasedOnOrAfter);
    }
}
