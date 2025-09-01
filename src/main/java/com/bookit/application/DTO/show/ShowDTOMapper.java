package com.bookit.application.DTO.show;

import com.bookit.application.entity.Show;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDTOMapper {

    public Show toShow(ShowDTO show){
        return new Show(show.getMovieId(),
                show.getTheatreId(),
                show.getLanguage(),
                show.getStartTime(),
                show.getEndTime());
    }

    public ShowDTO toDTO(Show show){
        return new ShowDTO(show.getTheatreExternalId(),
                show.getMovieExternalId(), show.getLanguage(), show.getStartTime(), show.getEndTime());
    }

    public List<ShowDTO> toDTO(List<Show> shows){
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
