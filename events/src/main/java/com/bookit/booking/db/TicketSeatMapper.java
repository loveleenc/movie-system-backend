package com.bookit.booking.db;

import com.bookit.booking.entity.Seat;
import com.bookit.booking.entity.Show;
import com.bookit.booking.entity.Ticket;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketSeatMapper implements RowMapper<Ticket> {

    private SeatMapper seatMapper;
    public TicketSeatMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Show show = new Show();
        Seat seat = this.seatMapper.getSeat(rs, "seatid", null);
        Ticket ticket = new Ticket(show,
                seat,
                rs.getString("status"),
                rs.getLong("price")
        );
        ticket.setId(rs.getString("ticketid"));
        ticket.setOwnerId(rs.getLong("owner"));
        return ticket;
    }
}
