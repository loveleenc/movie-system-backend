package com.bookit.booking.db;

import com.bookit.booking.entity.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class TicketMapper implements RowMapper<Ticket> {
    private SeatMapper seatMapper;

    public TicketMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {

        Theatre theatre = new Theatre(rs.getString("theatrename"), rs.getString("location"), rs.getInt("theatreid"));
        ShowTimeSlot showTimeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Movie movie = new Movie(rs.getString("name"),
                null,
                null,
                null,
                null,
                null,
                null);
        UUID showId = UUID.fromString(rs.getString("showid"));
        Show show = new Show(showTimeSlot, theatre, movie, rs.getString("showlanguage"), showId);
        Seat seat = this.seatMapper.getSeat(rs, "seatid", null);
        Ticket ticket = new Ticket(show, seat, rs.getString("status"), rs.getLong("price"));
        ticket.setId(rs.getString("ticketid"));
        ticket.setOwnerId(rs.getLong("ticketowner"));
        return ticket;
    }
}
