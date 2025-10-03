package com.bookit.application.dto.theatre;

import com.bookit.application.entity.Theatre;
import com.bookit.application.utils.TheatreUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreDtoMapper {

    public TheatreDto toTheatreDto(Theatre theatre){
        return new TheatreDto(theatre.getName(),
                theatre.getLocation(),
                theatre.getId());
    }

    public List<TheatreDto> toTheatreDto(List<Theatre> theatres){
        return theatres.stream().map(this::toTheatreDto).toList();
    }

    public Theatre toTheatre(TheatreDto theatreDto){
        Theatre theatre = new Theatre(theatreDto.getName(),
                theatreDto.getLocation(),
                1);
        theatre.setSeats(TheatreUtil.createSeats(theatreDto));
        return theatre;
    }
}
