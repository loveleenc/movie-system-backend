package com.bookit.application.DTO.movie;

import com.bookit.application.DTO.InvalidDataException;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieException;
import com.bookit.application.services.storage.StorageException;
import com.bookit.application.services.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDTOMapper {
    private final StorageService storageService;

    public MovieDTOMapper(StorageService storageService) {
        this.storageService = storageService;
    }

    public MovieDTO toDTO(Movie movie) throws MovieException {
        try {
            Resource resource = this.storageService.getResource(movie.getPoster());
            return new MovieDTOBuilder()
                    .setName(movie.getName())
                    .setDuration(movie.getDuration())
                    .setPoster(resource.getURL().toString())
                    .setGenreList(movie.getGenreList())
                    .setLanguages(movie.getLanguages())
                    .setReleaseDate(movie.getReleaseDate())
                    .setId(movie.getId())
                    .build();
        } catch (StorageException | IOException e) {
            throw new MovieException("Unable to fetch the movie", e);
        }
    }

    public List<MovieDTO> toDTO(List<Movie> movies) throws MovieException {
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Movie toMovie(MovieDTO movieDTO) throws InvalidDataException{
        List<String> unsetValues = this.anyUnsetValues(movieDTO.getName(), movieDTO.getDuration(), movieDTO.getPoster(), movieDTO.getGenreList(), movieDTO.getLanguages(), movieDTO.getReleaseDate().toString());
        if(!unsetValues.isEmpty()){
            throw new InvalidDataException("The following data appears to be missing: " + unsetValues);
        }
        return new Movie(movieDTO.getName(),
                movieDTO.getDuration(),
                movieDTO.getPoster(),
                movieDTO.getGenreList(),
                movieDTO.getReleaseDate(),
                movieDTO.getLanguages(),
                null);
    }

    public List<String> anyUnsetValues(String name, Integer duration, String poster, List<String> genreList, List<String> languages, String releaseDate){
        List<String> unsetValues = new ArrayList<>();
        if(name == null){
            unsetValues.add("name");
        }
        if(duration == null){
            unsetValues.add("duration");
        }

        if(poster == null){
            unsetValues.add("poster");
        }
        if(genreList == null){
            unsetValues.add("genre");
        }
        if(languages == null){
            unsetValues.add("language");
        }
        if(releaseDate == null){
            unsetValues.add("release date");
        }
        return unsetValues;
    }
}
