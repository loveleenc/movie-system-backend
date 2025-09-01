package com.bookit.application.services;

import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.DTO.show.ShowDTOMapper;
import com.bookit.application.entity.Movie;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Theatre;
import com.bookit.application.entity.TheatreTimeSlots;
import com.bookit.application.repository.MovieDAO;
import com.bookit.application.repository.ShowDAO;
import com.bookit.application.repository.TheatreDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class ShowService {
    private ShowDTOMapper showDTOMapper;
    private ShowDAO showDAO;
    private MovieDAO movieDAO;
    private TheatreDAO theatreDAO;

    public ShowService(ShowDTOMapper showDTOMapper, ShowDAO showDAO, MovieDAO movieDAO, TheatreDAO theatreDAO){
        this.showDTOMapper = showDTOMapper;
        this.showDAO = showDAO;
        this.movieDAO = movieDAO;
        this.theatreDAO = theatreDAO;
    }

    public List<ShowDTO> getShowsByMovie(String movieName, LocalDate movieReleaseDate){
        List<Show> shows = this.showDAO.findShowsByMovie(movieName, movieReleaseDate);
        return this.showDTOMapper.toDTO(shows);
    }

    public ShowDTO createShow(String movieExternalId, String theatreExternalId, Show show){
        Long movieId = this.movieDAO.findIdByExternalId(movieExternalId);
        Movie movie = this.movieDAO.findById(movieId);
        Long theatreId = this.theatreDAO.findIdByExternalId(theatreExternalId);

        if(!movie.getLanguages().contains(show.getLanguage())){
            //TODO: throw error
        }

        if(show.getStartTime().isBefore(movie.getReleaseDate().atStartOfDay())){
            //TODO: throw error
        }

        if(show.getDuration() < movie.getDuration()){
            //TODO: throw error
        }

        List<TheatreTimeSlots> unavailableTimeSlots = this.showDAO.getBookedSlotsByTheatreId(theatreId);
        if(!TheatreTimeSlots.noOverlapBetweenTimeSlotsExists(unavailableTimeSlots, show.getTimeSlot())){
            //TODO: throw error
        }

        show.setMovieId(movieId);
        show.setTheatreId(theatreId);
        Long id = this.showDAO.create(show);
        Show createdShow = this.showDAO.findById(id);
        return this.showDTOMapper.toDTO(createdShow);


    }
}
