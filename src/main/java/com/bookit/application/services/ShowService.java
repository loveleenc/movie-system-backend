package com.bookit.application.services;

import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.DTO.show.ShowDTOMapper;
import com.bookit.application.entity.Show;
import com.bookit.application.repository.ShowDAO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ShowService {
    private ShowDTOMapper showDTOMapper;
    private ShowDAO showDAO;

    public ShowService(ShowDTOMapper showDTOMapper, ShowDAO showDAO){
        this.showDTOMapper = showDTOMapper;
        this.showDAO = showDAO;
    }

    public List<ShowDTO> getShowsByMovie(String movieName, LocalDate movieReleaseDate){
        List<Show> shows = this.showDAO.findShowsByMovie(movieName, movieReleaseDate);
        return this.showDTOMapper.toDTO(shows);
    }


}
