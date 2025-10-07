package com.bookit.application.dto.ticket;

import com.bookit.application.dto.seat.SeatDto;
import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.types.MaskedTicketStatus;
import com.bookit.application.entity.Ticket;
import com.bookit.application.types.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketDtoMapper {
    public TicketDto toTicketDto(Ticket ticket) throws IllegalArgumentException {
        ShowDto showDTO = new ShowDto();

        SeatDto seatDTO = new SeatDto(ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getSeatType().code());
        String maskedStatus = TicketDtoMapper.maskActualTicketStatus(ticket);
        return new TicketDto(showDTO, seatDTO, maskedStatus, ticket.getPrice());
    }

    private static String maskActualTicketStatus(Ticket ticket){
        TicketStatus status = ticket.getStatus();
        String maskedStatus = null;
        switch(status){
            case AVAILABLE:
                maskedStatus = MaskedTicketStatus.AVAILABLE.code();
                break;
            case BLOCKED, CANCELLED, USED, BOOKED, RESERVED:
                maskedStatus = MaskedTicketStatus.BOOKED.code();
                break;
            default:
                throw new IllegalArgumentException(String.format("Ticket status %s unaccounted for and needs to be masked appropriately when returning data to users",
                        ticket.getStatus()));
        }
        return maskedStatus;
    }

    public List<TicketDto> toTicketDto(List<Ticket> tickets){
        return tickets.stream().map(this::toTicketDto).toList();
    }
}
