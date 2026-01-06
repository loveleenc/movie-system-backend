package com.bookit.application.showscheduling.dto.inbound.api;

import com.bookit.application.showscheduling.entity.Movie;
import com.bookit.application.showscheduling.entity.Show;
import com.bookit.application.showscheduling.entity.ShowTimeSlot;
import com.bookit.application.showscheduling.entity.Theatre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDtoMapper {

    public Show toShow(ShowDto showDto) {
        ShowTimeSlot timeSlot = new ShowTimeSlot(showDto.getStartTime(), showDto.getEndTime());
        Movie movie = new Movie(null,
                null,
                null,
                null,
                null,
                null,
                showDto.getMovieDto().getId());
        Theatre theatre = new Theatre(null, null, showDto.getTheatreDto().id());

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

        MovieDto movieDto = new MovieDto(
                movie.getName(),
                movie.getDuration(),
                movie.getPoster(),
                movie.getGenreList(),
                movie.getLanguages(),
                movie.getReleaseDate(),
                movie.getId()
        );

        return new ShowDto(theatreDTO, movieDto, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowDto> toDTO(List<Show> shows) {
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ShowTheatreDto toShowTheatreDTO(Show show) {
        com.bookit.application.showscheduling.entity.Theatre theatre = show.getTheatre();
        TheatreDto theatreDTO = new TheatreDto(theatre.getName(), theatre.getLocation(), theatre.getId());
        return new ShowTheatreDto(theatreDTO, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowTheatreDto> showTheatreDTO(List<Show> shows) {
        return shows.stream().map(this::toShowTheatreDTO).collect(Collectors.toList());
    }

    public ShowMovieNameDto toShowMovieNameDto(Show show) {
        return new ShowMovieNameDto(show.getStartTime(),
                show.getEndTime(),
                show.getLanguage(),
                show.getId().toString(),
                show.getMovie().getName());
    }

    public List<ShowMovieNameDto> toShowMovieNameDto(List<Show> shows) {
        return shows.stream().map(this::toShowMovieNameDto).collect(Collectors.toList());
    }
}
