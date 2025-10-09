package com.bookit.application.dto.show;

import com.bookit.application.dto.movie.MovieDto;
import com.bookit.application.dto.movie.MovieDtoBuilder;
import com.bookit.application.dto.theatre.TheatreDto;
import com.bookit.application.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDtoMapper {

    public Show toShow(ShowDto showDto) {
        ShowTimeSlot timeSlot = new ShowTimeSlot(showDto.getStartTime(), showDto.getEndTime());
        Movie movie = new MovieBuilder().setId(showDto.getMovieDto().getId()).build();
        Theatre theatre = new Theatre();
        theatre.setId(showDto.getTheatreDto().getId());

        return new Show(timeSlot,
                theatre,
                movie,
                showDto.getLanguage(),
                null);
    }

    public ShowDto toDTO(Show show) {
        TheatreDto theatreDTO = new TheatreDto(show.getTheatre().getName(),
                show.getTheatre().getLocation(),
                show.getTheatreId());

        Movie movie = show.getMovie();
        MovieDto movieDTO = new MovieDtoBuilder()
                .setName(movie.getName())
                .setDuration(movie.getDuration())
                .setPoster(movie.getPoster())
                .setGenreList(movie.getGenreList())
                .setLanguages(movie.getLanguages())
                .setReleaseDate(movie.getReleaseDate())
                .setId(movie.getId())
                .build();

        return new ShowDto(theatreDTO, movieDTO, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowDto> toDTO(List<Show> shows) {
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ShowDto toShowTheatreDTO(Show show) {
        Theatre theatre = show.getTheatre();
        TheatreDto theatreDTO = new TheatreDto(theatre.getName(), theatre.getLocation(), theatre.getId());
        MovieDto movieDTO = new MovieDtoBuilder().build();
        return new ShowDto(theatreDTO, movieDTO, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowDto> showTheatreDTO(List<Show> shows) {
        return shows.stream().map(this::toShowTheatreDTO).collect(Collectors.toList());
    }


}
