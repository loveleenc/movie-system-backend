package com.bookit.events.shows;

import com.bookit.events.shows.booking.BookingClient;
import com.bookit.events.shows.db.IShowDao;
import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.entity.Show;
import com.bookit.events.shows.entity.ShowTimeSlot;
import com.bookit.events.shows.movie.MovieClient;
import com.bookit.events.shows.user.UserClient;
import com.bookit.events.shows.entity.types.TicketStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ShowService {
    private IShowDao showDAO;
    private UserClient userClient;
    private MovieClient movieClient;
    private BookingClient bookingClient;


    public ShowService(IShowDao showDAO,
                       UserClient userClient,
                       @Qualifier("showsMovieApiClient") MovieClient movieClient,
                       BookingClient bookingClient) {
        this.showDAO = showDAO;
        this.userClient = userClient;
        this.movieClient = movieClient;
        this.bookingClient = bookingClient;
    }

    public List<Show> getShowsByMovie(@NonNull Long movieId) {
        return this.showDAO.findShowsByMovie(movieId);
    }

    public List<Show> getShowsByTheatre(@NonNull Integer theatreId) throws ResourceNotFoundException {
        Long userId = this.userClient.getCurrentUserId();
        return this.showDAO.findShowsByTheatre(theatreId, userId);
    }
    
    public Show createShowAndTickets(Show show, Long moviePrice, String ticketStatus) throws NullPointerException, ResourceCreationException {
        Show createdShow = this.createShow(show);
        this.bookingClient.createTickets(Objects.requireNonNull(moviePrice), createdShow, Objects.requireNonNull(ticketStatus));
        return createdShow;
    }

    private Show createShow(Show show) throws ResourceCreationException {
        Long movieId = show.getMovie().getId();
        Movie movie = this.movieClient.getMovieById(movieId);
        Integer theatreId = show.getTheatreId();

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
        Show createdShow = this.showDAO.findById(id);
        createdShow.setMovie(movie);
        return createdShow;
    }

    public void cancelShow(String showId) {
        this.bookingClient.updateTicketStatusForShow(showId, TicketStatus.CANCELLED.code(), true);
    }

    public Show createTicketsForExistingShow(String showId, Long moviePrice, String ticketStatus){
        Show show = this.showDAO.findById(showId);
        this.bookingClient.createTickets(Objects.requireNonNull(moviePrice), show, Objects.requireNonNull(ticketStatus));
        return show;
    }

}
