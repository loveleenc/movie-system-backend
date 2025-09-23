package com.bookit.application.services;

import com.bookit.application.entity.*;
import com.bookit.application.persistence.IMovieDao;
import com.bookit.application.persistence.IShowDao;
import com.bookit.application.types.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowService {
    private IShowDao showDAO;
    private IMovieDao movieDAO;
    private TicketService ticketService;

    public ShowService(IShowDao showDAO, IMovieDao movieDAO, TicketService ticketService){
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

        String id = this.showDAO.create(show);
        return this.showDAO.findById(id);
    }

    public void cancelShow(String showId){
        this.ticketService.updateTicketStatusForShow(showId, TicketStatus.CANCELLED.code(), true);
    }

}
