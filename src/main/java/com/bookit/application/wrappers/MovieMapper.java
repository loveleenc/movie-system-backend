package com.bookit.application.wrappers;

import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.entity.Movie;
import com.bookit.application.repository.blob.MoviePosterBlob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    private MoviePosterBlob moviePosterBlob;

    public MovieMapper(MoviePosterBlob moviePosterBlob){
        this.moviePosterBlob = moviePosterBlob;
    }

    public MovieDTO toDTO(Movie movie){
        MovieDTO movieDTO = new MovieDTO(movie);
        movieDTO.setPoster(this.moviePosterBlob.getBlobPath(movie.getPoster()));
        return movieDTO;
    }

    public List<MovieDTO> transformAllMovies(List<Movie> movies){
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
