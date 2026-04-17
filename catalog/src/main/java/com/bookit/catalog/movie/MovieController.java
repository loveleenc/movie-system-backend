package com.bookit.catalog.movie;


import com.bookit.catalog.movie.inbound.api.MovieDto;
import com.bookit.catalog.movie.inbound.api.MovieDtoMapper;
import com.bookit.catalog.movie.inbound.service.MoviePageServiceDto;
import com.bookit.catalog.movie.inbound.service.MovieServiceDto;
import com.bookit.catalog.movie.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class MovieController {
    private final static String START_DATE = "1970-01-01";
    private final MovieService movieService;
    private final MovieDtoMapper movieDTOMapper;


    MovieController(MovieService movieService, MovieDtoMapper movieDTOMapper){
        this.movieService = movieService;
        this.movieDTOMapper = movieDTOMapper;
    }

    @GetMapping("/movies/{page}")
    MoviePageServiceDto getAllMovies(@PathVariable Integer page,
                                     @RequestParam(required = true) Integer perPageCount) throws ExecutionException, InterruptedException {
        return this.movieService.getMovies(page, perPageCount);
    }

    @GetMapping("/movies/ongoing")
    ResponseEntity<List<MovieServiceDto>> getOngoingMovies() throws ExecutionException, InterruptedException {
        List<MovieServiceDto> movies = this.movieService.getOngoingMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/movies/upcoming")
    ResponseEntity<List<MovieServiceDto>> getUpcomingMovies() throws ExecutionException, InterruptedException {
      List<MovieServiceDto> movies = this.movieService.getUpcomingMovies();
      return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/movies/filter")
    ResponseEntity<List<MovieServiceDto>> filterMovies(@RequestParam(required = false) List<String> genre,
                                                @RequestParam(required = false) List<String> language,
                                                @RequestParam(required = false) String releasedOnOrAfter) throws ExecutionException, InterruptedException {
        if(genre == null){
            genre = new ArrayList<>();
        }
        if(language == null){
            language = new ArrayList<>();
        }
        if(releasedOnOrAfter == null){
            releasedOnOrAfter = START_DATE;
        }
        List<MovieServiceDto> movies = this.movieService.filterMovies(genre, language, releasedOnOrAfter);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/com/bookit/movie")
    ResponseEntity<MovieDto> addMovie(@RequestPart MovieDto movieData, @RequestPart MultipartFile file){
        Objects.requireNonNull(file);
        MovieServiceDto movie = this.movieDTOMapper.toMovie(movieData);
        MovieServiceDto createdMovie = this.movieService.addMovie(movie, file);
        return new ResponseEntity<>(this.movieDTOMapper.toDTO(createdMovie), HttpStatus.CREATED);
    }
    //TODO: update movie details
}
