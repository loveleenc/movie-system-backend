package com.bookit.application.DTO.show;

import com.bookit.application.entity.Show;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowDTOMapper {

    public ShowDTOMapper(){}

    public ShowDTO toDTO(Show show){
        return new ShowDTO(show);
    }

    public List<ShowDTO> toDTO(List<Show> shows){
        return shows.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
