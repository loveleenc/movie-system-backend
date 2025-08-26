package com.bookit.application.DTO.movie;

import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieException;
import com.bookit.application.services.storage.StorageException;
import com.bookit.application.services.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDTOMapper {
    private StorageService storageService;

    public MovieDTOMapper(StorageService storageService){
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

    public List<MovieDTO> toDTO(List<Movie> movies) throws MovieException{
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
