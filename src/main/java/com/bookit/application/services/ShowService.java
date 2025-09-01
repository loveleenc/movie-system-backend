package com.bookit.application.services;

import com.bookit.application.entity.Movie;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Theatre;
import com.bookit.application.entity.TheatreTimeSlots;
import com.bookit.application.repository.MovieDAO;
import com.bookit.application.repository.ShowDAO;
import com.bookit.application.repository.TheatreDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowService {
    private ShowDAO showDAO;
    private MovieDAO movieDAO;
    private TheatreDAO theatreDAO;

    public ShowService(ShowDAO showDAO, MovieDAO movieDAO, TheatreDAO theatreDAO){
        this.showDAO = showDAO;
        this.movieDAO = movieDAO;
        this.theatreDAO = theatreDAO;
    }

    public List<Show> getShowsByMovie(String movieExternalId){
        Long movieId = this.movieDAO.findIdByExternalId(movieExternalId);
        return this.showDAO.findShowsByMovie(movieId);
    }

    public Show createShow(Show show){
        Long movieId = this.movieDAO.findIdByExternalId(show.getMovieExternalId());
        Movie movie = this.movieDAO.findById(movieId);
        Long theatreId = this.theatreDAO.findIdByExternalId(show.getTheatreExternalId());

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
        createdShow.setMovieExternalId(show.getMovieExternalId());
        createdShow.setTheatreExternalId(show.getTheatreExternalId());
        return createdShow;
    }
}
