package com.bookit.application.DTO.ticket;

import com.bookit.application.DTO.seat.SeatDTO;
import com.bookit.application.DTO.show.ShowDTO;
import com.bookit.application.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketDTOMapper {
    public TicketDTO toTicketForShowDTO(Ticket ticket) {
        ShowDTO showDTO = new ShowDTO();

        SeatDTO seatDTO = new SeatDTO(ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getSeatType(),
                ticket.getSeat().getSeatPrice(),
                ticket.getSeat().getId());
        return new TicketDTO(showDTO, seatDTO, ticket.getStatus(), ticket.getPrice());
    }


}
