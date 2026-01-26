package com.bookit.catalog.movie;

import com.bookit.catalog.movie.inbound.service.MovieServiceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MovieControllerInternal {
    private final MovieService movieService;

    public MovieControllerInternal(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/internal/movie/{id}")
    public ResponseEntity<MovieServiceDto> getMovieById(@PathVariable Long id){
        return new ResponseEntity<>(this.movieService.findMovie(id), HttpStatus.OK);
    }
}
