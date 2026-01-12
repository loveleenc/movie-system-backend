package com.bookit.catalog.movie.inbound.service;

import com.bookit.catalog.movie.MovieException;
import com.bookit.catalog.movie.entity.Movie;
import com.bookit.catalog.movie.storage.StorageException;
import com.bookit.catalog.movie.storage.StorageService;
import com.bookit.catalog.movie.storage.resource.PosterResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieServiceDtoMapper {
    private final StorageService storageService;

    public MovieServiceDtoMapper(StorageService storageService) {
        this.storageService = storageService;
    }

    public MovieServiceDto toDTO(Movie movie) throws MovieException {
        try {
            PosterResource resource = this.storageService.getResource(movie.getPoster());
            return new MovieServiceDtoBuilder()
                    .setName(movie.getName())
                    .setDuration(movie.getDuration())
                    .setPoster(resource.getContentOrUrlAsString())
                    .setGenreList(movie.getGenreList())
                    .setLanguages(movie.getLanguages())
                    .setReleaseDate(movie.getReleaseDate())
                    .setId(movie.getId())
                    .build();
        } catch (StorageException e) {
            throw new MovieException("Unable to fetch the movie", e);
        }
    }

    public List<MovieServiceDto> toDTO(List<Movie> movies) throws MovieException {
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Movie toMovie(MovieServiceDto movieDTO) throws NullPointerException{
        return new Movie(movieDTO.getName(),
                movieDTO.getDuration(),
                movieDTO.getPoster(),
                movieDTO.getGenreList(),
                movieDTO.getReleaseDate(),
                movieDTO.getLanguages(),
                null);
    }
}
