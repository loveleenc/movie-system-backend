package com.bookit.application.moviecatalog;


import com.bookit.application.moviecatalog.dto.inbound.api.MovieDto;
import com.bookit.application.moviecatalog.dto.inbound.api.MovieDtoMapper;
import com.bookit.application.moviecatalog.dto.inbound.service.MovieServiceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @GetMapping("/movies")
    List<MovieDto> getAllMovies(){
        List<MovieServiceDto> movies = this.movieService.getMovies();
        return movieDTOMapper.toDTO(movies);
    }

    @GetMapping("/movies/ongoing")
    ResponseEntity<List<MovieDto>> getOngoingMovies(){
        List<MovieServiceDto> movies = this.movieService.getOngoingMovies();
        return new ResponseEntity<>(this.movieDTOMapper.toDTO(movies), HttpStatus.OK);
    }

    @GetMapping("/movies/upcoming")
    ResponseEntity<List<MovieDto>> getUpcomingMovies(){
      List<MovieServiceDto> movies = this.movieService.getUpcomingMovies();
      return new ResponseEntity<>(this.movieDTOMapper.toDTO(movies), HttpStatus.OK);
    }

    @GetMapping("/movies/filter")
    ResponseEntity<List<MovieDto>> filterMovies(@RequestParam(required = false) List<String> genre,
                                                @RequestParam(required = false) List<String> language,
                                                @RequestParam(required = false) String releasedOnOrAfter){
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
        return new ResponseEntity<>(this.movieDTOMapper.toDTO(movies), HttpStatus.OK);
    }

    @PostMapping("/movie")
    ResponseEntity<MovieDto> addMovie(@RequestPart MovieDto movieData, @RequestPart MultipartFile file){
        Objects.requireNonNull(file);
        MovieServiceDto movie = this.movieDTOMapper.toMovie(movieData);
        MovieServiceDto createdMovie = this.movieService.addMovie(movie, file);
        return new ResponseEntity<>(this.movieDTOMapper.toDTO(createdMovie), HttpStatus.CREATED);
    }
    //TODO: update movie details
}
