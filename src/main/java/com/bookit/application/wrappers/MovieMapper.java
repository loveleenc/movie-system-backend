package com.bookit.application.wrappers;

import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieException;
import com.bookit.application.storage.StorageException;
import com.bookit.application.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    private StorageService storageService;

    public MovieMapper(StorageService storageService){
        this.storageService = storageService;
    }

    public MovieDTO toDTO(Movie movie) throws MovieException{
        try{
            MovieDTO movieDTO = new MovieDTO(movie);
            Resource resource = this.storageService.getResource(movie.getPoster());
            movieDTO.setPoster(resource.getURL().toString());
            return movieDTO;
        }
        catch(StorageException | IOException e){
            throw new MovieException("Unable to fetch the movie", e);
        }
    }

    public List<MovieDTO> transformAllMovies(List<Movie> movies){
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
