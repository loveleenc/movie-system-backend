package com.bookit.application.controller.movie;


import com.bookit.application.dto.movie.MovieDto;
import com.bookit.application.dto.movie.MovieDtoMapper;
import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.show.ShowDtoMapper;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieService;
import com.bookit.application.services.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class MovieController {
    private final static String START_DATE = "1970-01-01";
    private final MovieService movieService;
    private final MovieDtoMapper movieDTOMapper;
    private final ShowService showService;
    private final ShowDtoMapper showDTOMapper;


    MovieController(MovieService movieService, MovieDtoMapper movieDTOMapper, ShowService showService, ShowDtoMapper showDTOMapper){
        this.movieService = movieService;
        this.movieDTOMapper = movieDTOMapper;
        this.showService = showService;
        this.showDTOMapper = showDTOMapper;
    }
//    @GetMapping("/")
//    public String listUploadedFiles(Model model) {
//        return "uploadForm";
//    }

    @GetMapping("/movies")
    List<MovieDto> getAllMovies(){
        return this.movieService.getMovies().stream().map(this.movieDTOMapper::toDTO).toList();
    }

    @GetMapping("/movies/ongoing")
    ResponseEntity<List<MovieDto>> getOngoingMovies(){
        return new ResponseEntity<>(this.movieService.getOngoingMovies().stream().map(this.movieDTOMapper::toDTO).toList(), HttpStatus.OK);
    }

    @GetMapping("/movies/upcoming")
    ResponseEntity<List<MovieDto>> getUpcomingMovies(){
        return new ResponseEntity<>(this.movieService.getUpcomingMovies().stream().map(this.movieDTOMapper::toDTO).toList(), HttpStatus.OK);
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
        List<MovieDto> movies = this.movieService.filterMovies(genre, language, releasedOnOrAfter).stream().map(this.movieDTOMapper::toDTO).toList();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/movie")
    ResponseEntity<MovieDto> addMovie(@RequestPart MovieDto movieData, @RequestPart MultipartFile file){
        Objects.requireNonNull(file);
        Movie movie = this.movieDTOMapper.toMovie(movieData);
        Movie createdMovie = this.movieService.addMovie(movie, file);
        return new ResponseEntity<>(this.movieDTOMapper.toDTO(createdMovie), HttpStatus.CREATED);
    }

    @GetMapping("/movie/{id}/shows")
    ResponseEntity<List<ShowDto>> getShowsByMovie(@PathVariable Long id){
        List<ShowDto> shows = this.showService.getShowsByMovie(id).stream().map(this.showDTOMapper::toShowTheatreDTO).toList();
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    //TODO: update movie details
}
