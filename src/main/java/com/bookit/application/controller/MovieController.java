package com.bookit.application.controller;


import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieService;
import com.bookit.application.types.MovieGenre;
import com.bookit.application.types.MovieLanguage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;

    MovieController(MovieService movieService){
        this.movieService = movieService;
    }

//    @GetMapping("/")
//    public String listUploadedFiles(Model model) throws IOException {
//        return "uploadForm";
//    }

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

    @PostMapping("/movie")
    MovieDTO addMovie(@RequestPart Movie movie, @RequestPart MultipartFile file){
        return this.movieService.addMovie(movie, file);
    }

}
