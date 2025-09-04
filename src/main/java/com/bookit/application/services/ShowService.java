package com.bookit.application.services;

import com.bookit.application.entity.*;
import com.bookit.application.dao.MovieDao;
import com.bookit.application.dao.ShowDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowService {
    private ShowDao showDAO;
    private MovieDao movieDAO;
    private TicketService ticketService;

    public ShowService(ShowDao showDAO, MovieDao movieDAO, TicketService ticketService){
        this.showDAO = showDAO;
        this.movieDAO = movieDAO;
        this.ticketService = ticketService;
    }

    public List<Show> getShowsByMovie(Long movieId){
        return this.showDAO.findShowsByMovie(movieId);
    }

    public Show createShowAndTickets(Show show, Long moviePrice, String ticketStatus){
        Show createdShow = this.createShow(show);
        if(moviePrice != null && ticketStatus != null){
            this.ticketService.createTicketsForShow(moviePrice, createdShow, ticketStatus);
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

        String id = this.showDAO.createShow(show);
        return this.showDAO.findByShowId(id);
    }
}
