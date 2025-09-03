package com.bookit.application.services;

import com.bookit.application.entity.*;
import com.bookit.application.repository.MovieDAO;
import com.bookit.application.repository.ShowDAO;
import com.bookit.application.repository.TheatreDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    public List<Show> getShowsByMovie(Long movieId){
        return this.showDAO.findShowsByMovie(movieId);
    }

    public Show createShowAndTickets(Show show, Ticket ticket){
        Show createdShow = this.createShow(show);
        if(ticket != null){
            //TODO: create tickets here bruh
            ticket.getTheatre().setId(createdShow.getTheatreId());

        }
        return createdShow;
    }

    private Show createShow(Show show){
        Long movieId = show.getMovieId();
        Movie movie = this.movieDAO.findById(movieId);
        Long theatreId = show.getTheatreId();

        if(!movie.getLanguages().contains(show.getLanguage())){
            //TODO: throw error
        }

        if(show.getStartTime().isBefore(movie.getReleaseDate().atStartOfDay())){
            //TODO: throw error
        }

        if(show.getDuration() < movie.getDuration()){
            //TODO: throw error
        }

        List<ShowTimeSlot> unavailableTimeSlots = this.showDAO.getBookedSlotsByTheatreId(theatreId);
        if(!ShowTimeSlot.noOverlapBetweenTimeSlotsExists(unavailableTimeSlots, show.getTimeSlot())){
            //TODO: throw error
        }

        //TODO: throw error if value is not 1
        String id = this.showDAO.createShow(show);
        return this.showDAO.findByShowId(id);
    }
}
