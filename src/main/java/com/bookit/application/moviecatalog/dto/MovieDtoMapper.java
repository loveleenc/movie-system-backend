package com.bookit.application.moviecatalog.dto;

import com.bookit.application.moviecatalog.MovieException;
import com.bookit.application.moviecatalog.service.MovieServiceDto;
import com.bookit.application.moviecatalog.service.MovieServiceDtoBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDtoMapper {

    public MovieDtoMapper() {
    }

    public MovieDto toDTO(MovieServiceDto movie) throws MovieException {
      return new MovieDtoBuilder()
        .setName(movie.getName())
        .setDuration(movie.getDuration())
        .setPoster(movie.getPoster())
        .setGenreList(movie.getGenreList())
        .setLanguages(movie.getLanguages())
        .setReleaseDate(movie.getReleaseDate())
        .setId(movie.getId())
        .build();
    }

    public List<MovieDto> toDTO(List<MovieServiceDto> movies) throws MovieException {
        return movies.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MovieServiceDto toMovie(MovieDto movieDTO) throws NullPointerException{
        return new MovieServiceDtoBuilder()
          .setName(movieDTO.getName())
          .setPoster(movieDTO.getPoster())
          .setGenreList(movieDTO.getGenreList())
          .setReleaseDate(movieDTO.getReleaseDate())
          .setLanguages(movieDTO.getLanguages())
          .setDuration(movieDTO.getDuration())
          .setId(null)
          .build();
    }
}
