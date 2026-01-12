package com.bookit.events.shows.booking.local;

import com.bookit.booking.TicketService;
import com.bookit.booking.entity.Movie;
import com.bookit.booking.entity.ShowTimeSlot;
import com.bookit.booking.entity.Theatre;
import com.bookit.events.shows.booking.BookingClient;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import com.bookit.events.shows.entity.Show;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

class ShowMapper {
    public static com.bookit.booking.entity.Show toBookingShow(Show createdShow) {
        com.bookit.booking.entity.ShowTimeSlot showTimeSlot = new ShowTimeSlot(createdShow.getStartTime(), createdShow.getEndTime());
        com.bookit.booking.entity.Theatre showTheatre = new Theatre(createdShow.getTheatre().getName(),
                createdShow.getTheatre().getLocation(),
                createdShow.getTheatre().getId());

        com.bookit.events.shows.entity.Movie selectedMovie = createdShow.getMovie();
        com.bookit.booking.entity.Movie movie = new Movie(
                selectedMovie.getName(),
                selectedMovie.getDuration(),
                selectedMovie.getPoster(),
                selectedMovie.getGenreList(),
                selectedMovie.getReleaseDate(),
                selectedMovie.getLanguages(),
                selectedMovie.getId()
        );
        return new com.bookit.booking.entity.Show(
                showTimeSlot,
                showTheatre,
                movie,
                createdShow.getLanguage(),
                createdShow.getId()
                );
    }
}

@Component("showsBookingLocalClient")
@ComponentScan(basePackages = {"com.bookit.booking"})
public class BookingLocalClient implements BookingClient {
    private TicketService ticketService;

    public BookingLocalClient(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void createTickets(Long moviePrice, Show show, String ticketStatus) {
        com.bookit.booking.entity.Show createdShow = ShowMapper.toBookingShow(show);
        this.ticketService.createTickets(moviePrice, createdShow, ticketStatus);
    }

    @Override
    public void updateTicketStatusForShow(String showId, String ticketStatus, Boolean overrideStatusChangeValidation) {
        this.ticketService.updateTicketStatusForShow(showId, ticketStatus, overrideStatusChangeValidation);
    }

    @Override
    public void sendRequest(Request request) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Object processResponse(Response response) {
        throw new RuntimeException("Not implemented yet");
    }
}
