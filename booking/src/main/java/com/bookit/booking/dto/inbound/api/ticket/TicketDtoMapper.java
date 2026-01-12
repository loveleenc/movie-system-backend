package com.bookit.booking.dto.inbound.api.ticket;

import com.bookit.booking.dto.inbound.api.MovieDto;
import com.bookit.booking.dto.inbound.api.SeatDto;
import com.bookit.booking.dto.inbound.api.ShowDto;
import com.bookit.booking.dto.inbound.api.TheatreDto;
import com.bookit.booking.dto.inbound.api.types.MaskedTicketStatus;
import com.bookit.booking.entity.Show;
import com.bookit.booking.entity.Ticket;
import com.bookit.booking.entity.types.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketDtoMapper {
    private static String maskActualTicketStatus(Ticket ticket) {
        TicketStatus status = ticket.getStatus();
        String maskedStatus = null;
        switch (status) {
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

    public TicketDto toTicketDto(Ticket ticket, Boolean statusHidden, ShowDto showDto){
        SeatDto seatDTO = new SeatDto(ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getSeatType().code());
        String maskedStatus;
        if (statusHidden) {
            maskedStatus = TicketDtoMapper.maskActualTicketStatus(ticket);
        } else {
            maskedStatus = ticket.getStatus().code();
        }
        return new TicketDto(showDto, seatDTO, maskedStatus, ticket.getPrice(), ticket.getId());
    }

    public TicketDto toTicketDto(Ticket ticket, Boolean statusHidden, Show show) {
        SeatDto seatDTO = new SeatDto(ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getSeatType().code());
        String maskedStatus;
        if (statusHidden) {
            maskedStatus = TicketDtoMapper.maskActualTicketStatus(ticket);
        } else {
            maskedStatus = ticket.getStatus().code();
        }
        ShowDto showDto = new ShowDto(
                new TheatreDto(show.getTheatre().getName(), show.getTheatre().getLocation(), show.getTheatre().getId()),
                new MovieDto(show.getMovie().getName(),
                        show.getMovie().getDuration(),
                        show.getMovie().getPoster(),
                        show.getMovie().getGenreList(),
                        show.getMovie().getLanguages(),
                        show.getMovie().getReleaseDate(),
                        show.getMovie().getId()),
                show.getStartTime(),
                show.getEndTime(),
                show.getLanguage(),
                show.getId().toString()
        );

        return new TicketDto(showDto, seatDTO, maskedStatus, ticket.getPrice(), ticket.getId());
    }

    public TicketDto toTicketDto(Ticket ticket, Boolean statusHidden) throws IllegalArgumentException {
        ShowDto showDTO = new ShowDto();
        return this.toTicketDto(ticket, statusHidden, showDTO);
    }

    public List<TicketDto> toTicketDto(List<Ticket> tickets, Boolean statusHidden) {
        return tickets.stream().map(ticket -> this.toTicketDto(ticket, statusHidden)).toList();
    }
}
