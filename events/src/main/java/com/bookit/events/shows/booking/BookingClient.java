package com.bookit.events.shows.booking;

import com.bookit.booking.entity.types.TicketStatus;
import com.bookit.events.shows.comms.Client;
import com.bookit.events.shows.entity.Show;

public interface BookingClient extends Client {
    void createTickets(Long moviePrice, Show show, String ticketStatus);
    void updateTicketStatusForShow(String showId, String ticketStatus, Boolean overrideStatusChangeValidation);
}
