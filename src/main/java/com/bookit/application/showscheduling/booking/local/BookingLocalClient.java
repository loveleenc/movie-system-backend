package com.bookit.application.showscheduling.booking.local;

import com.bookit.application.booking.TicketService;
import com.bookit.application.booking.entity.Movie;
import com.bookit.application.booking.entity.ShowTimeSlot;
import com.bookit.application.booking.entity.Theatre;
import com.bookit.application.booking.entity.types.TicketStatus;
import com.bookit.application.showscheduling.booking.BookingClient;
import com.bookit.application.showscheduling.comms.Request;
import com.bookit.application.showscheduling.comms.Response;
import com.bookit.application.showscheduling.entity.Show;
import org.springframework.stereotype.Component;

import java.util.UUID;

class ShowMapper {
    public static com.bookit.application.booking.entity.Show toBookingShow(Show createdShow) {
        com.bookit.application.booking.entity.ShowTimeSlot showTimeSlot = new ShowTimeSlot(createdShow.getStartTime(), createdShow.getEndTime());
        com.bookit.application.booking.entity.Theatre showTheatre = new Theatre(createdShow.getTheatre().getName(),
                createdShow.getTheatre().getLocation(),
                createdShow.getTheatre().getId());

        com.bookit.application.showscheduling.entity.Movie selectedMovie = createdShow.getMovie();
        com.bookit.application.booking.entity.Movie movie = new Movie(
                selectedMovie.getName(),
                selectedMovie.getDuration(),
                selectedMovie.getPoster(),
                selectedMovie.getGenreList(),
                selectedMovie.getReleaseDate(),
                selectedMovie.getLanguages(),
                selectedMovie.getId()
        );
        return new com.bookit.application.booking.entity.Show(
                showTimeSlot,
                showTheatre,
                movie,
                createdShow.getLanguage(),
                createdShow.getId()
                );
    }
}

@Component("showsBookingLocalClient")
public class BookingLocalClient implements BookingClient {
    private TicketService ticketService;

    public BookingLocalClient(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void createTickets(Long moviePrice, Show show, String ticketStatus) {
        com.bookit.application.booking.entity.Show createdShow = ShowMapper.toBookingShow(show);
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
