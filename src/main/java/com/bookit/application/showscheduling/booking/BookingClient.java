package com.bookit.application.showscheduling.booking;

import com.bookit.application.booking.entity.types.TicketStatus;
import com.bookit.application.showscheduling.comms.Client;
import com.bookit.application.showscheduling.entity.Show;

import java.util.UUID;

public interface BookingClient extends Client {
    void createTickets(Long moviePrice, Show show, String ticketStatus);
    void updateTicketStatusForShow(String showId, String ticketStatus, Boolean overrideStatusChangeValidation);
}
