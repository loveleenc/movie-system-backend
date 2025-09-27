package com.bookit.application.services;

import com.bookit.application.entity.*;
import com.bookit.application.persistence.IMovieDao;
import com.bookit.application.persistence.IShowDao;
import com.bookit.application.types.TicketStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ShowService {
    private IShowDao showDAO;
    private IMovieDao movieDAO;
    private TicketService ticketService;

    public ShowService(IShowDao showDAO, IMovieDao movieDAO, TicketService ticketService) {
        this.showDAO = showDAO;
        this.movieDAO = movieDAO;
        this.ticketService = ticketService;
    }

    public List<Show> getShowsByMovie(@NonNull Long movieId) {
        return this.showDAO.findShowsByMovie(movieId);
    }

    public Show createShowAndTickets(Show show, Long moviePrice, String ticketStatus) throws NullPointerException, ResourceCreationException {
        Show createdShow = this.createShow(show);
        this.ticketService.createTickets(Objects.requireNonNull(moviePrice), createdShow, Objects.requireNonNull(ticketStatus));
        return createdShow;
    }

    private Show createShow(Show show) throws ResourceCreationException{
        Long movieId = show.getMovieId();
        Movie movie = this.movieDAO.findById(movieId);
        Long theatreId = show.getTheatreId();

        if (!movie.getLanguages().contains(show.getLanguage())) {
            throw new ResourceCreationException("Movie is not available in the language selected for the show to be created");
        }

        if (show.getStartTime().isBefore(movie.getReleaseDate().atStartOfDay())) {
            throw new ResourceCreationException("Show cannot be created for a date that is before the movie release date");
        }

        if (show.getDuration() < movie.getDuration()) {
            throw new ResourceCreationException("Show duration cannot be less than the duration of the movie");
        }

        List<ShowTimeSlot> unavailableTimeSlots = this.showDAO.getBookedSlotsByTheatreId(theatreId);
        if (!ShowTimeSlot.noOverlapBetweenTimeSlotsExists(unavailableTimeSlots, show.getTimeSlot())) {
            throw new ResourceCreationException("Selected show time slot overlaps with the timeslot of another show");
        }

        String id = this.showDAO.create(show);
        return this.showDAO.findById(id);
    }

    public void cancelShow(String showId) {
        this.ticketService.updateTicketStatusForShow(showId, TicketStatus.CANCELLED.code(), true);
    }

}
