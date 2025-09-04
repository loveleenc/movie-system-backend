package com.bookit.application.dto.ticket;

import com.bookit.application.dto.seat.SeatDto;
import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketDtoMapper {
    public TicketDto toTicketForShowDTO(Ticket ticket) {
        ShowDto showDTO = new ShowDto();

        SeatDto seatDTO = new SeatDto(ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getSeatType(),
                ticket.getSeat().getSeatPrice(),
                ticket.getSeat().getId());
        return new TicketDto(showDTO, seatDTO, ticket.getStatus(), ticket.getPrice());
    }


}
