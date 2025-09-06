package com.bookit.application.persistence.mappers;

import com.bookit.application.entity.Seat;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.Ticket;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketMapper implements RowMapper<Ticket> {

    private SeatMapper seatMapper;
    public TicketMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Show show = new Show();
        Seat seat = this.seatMapper.getSeat(rs, "seatid", null);
        return new Ticket(show,
                seat,
                rs.getString("status"),
                rs.getLong("price")
        );
    }
}
