package com.bookit.application.DTO.show;

import com.bookit.application.DTO.theatre.TheatreDTO;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.TheatreTimeSlots;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDTOMapper {

    public Show toShow(ShowDTO show){
        TheatreTimeSlots timeSlot = new TheatreTimeSlots(show.getStartTime(), show.getEndTime());
        return new Show(show.getMovieId(),
                show.getTheatreId(),
                show.getLanguage(),
                timeSlot);
    }

    public ShowDTO toDTO(Show show){
        return new ShowDTO(show.getTheatreExternalId(),
                show.getMovieExternalId(), show.getLanguage(), show.getStartTime(), show.getEndTime());
    }

    public List<ShowDTO> toDTO(List<Show> shows){
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ShowDTO toShowTheatreDTO(Show show){
        TheatreDTO theatreDTO = new TheatreDTO(show.getTheatre().getName(), show.getTheatre().getLocation());
        return new ShowDTO(show.getLanguage(), show.getStartTime(), show.getEndTime(), theatreDTO);
    }

    public List<ShowDTO> showTheatreDTO(List<Show> shows){
        return shows.stream().map(this::toShowTheatreDTO).collect(Collectors.toList());
    }


}
