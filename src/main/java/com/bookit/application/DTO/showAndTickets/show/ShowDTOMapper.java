package com.bookit.application.DTO.showAndTickets.show;

import com.bookit.application.DTO.movie.MovieDTO;
import com.bookit.application.DTO.movie.MovieDTOBuilder;
import com.bookit.application.DTO.theatre.TheatreDTO;
import com.bookit.application.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDTOMapper {

    public Show toShow(ShowDTO showDto) {
        ShowTimeSlot timeSlot = new ShowTimeSlot(showDto.getStartTime(), showDto.getEndTime());
        Movie movie = new MovieBuilder().setId(showDto.getMovie().getId()).build();
        Theatre theatre = new Theatre();
        theatre.setId(showDto.getTheatre().getId());

        return new Show(timeSlot,
                theatre,
                movie,
                showDto.getLanguage(),
                null);
    }

    public ShowDTO toDTO(Show show) {
        TheatreDTO theatreDTO = new TheatreDTO(show.getTheatre().getName(),
                show.getTheatre().getLocation(),
                show.getTheatreId());

        Movie movie = show.getMovie();
        MovieDTO movieDTO = new MovieDTOBuilder()
                .setName(movie.getName())
                .setDuration(movie.getDuration())
                .setPoster(movie.getPoster())
                .setGenreList(movie.getGenreList())
                .setLanguages(movie.getLanguages())
                .setReleaseDate(movie.getReleaseDate())
                .setId(movie.getId())
                .build();

        return new ShowDTO(theatreDTO, movieDTO, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowDTO> toDTO(List<Show> shows) {
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ShowDTO toShowTheatreDTO(Show show) {
        Theatre theatre = show.getTheatre();
        TheatreDTO theatreDTO = new TheatreDTO(theatre.getName(), theatre.getLocation(), theatre.getId());
        MovieDTO movieDTO = new MovieDTOBuilder().build();
        return new ShowDTO(theatreDTO, movieDTO, show.getStartTime(), show.getEndTime(), show.getLanguage(), show.getId().toString());
    }

    public List<ShowDTO> showTheatreDTO(List<Show> shows) {
        return shows.stream().map(this::toShowTheatreDTO).collect(Collectors.toList());
    }


}
