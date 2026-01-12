package com.bookit.booking.db;

import com.bookit.booking.entity.Seat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SeatMapper implements RowMapper<Seat> {
    @Override
    public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.getSeat(rs, "id", rs.getLong("seatprice"));
    }

    public Seat getSeat(ResultSet rs, String idColumnName, Long seatPrice) throws SQLException{
        return new Seat(rs.getString("seatnumber"),
                rs.getString("seattype"),
                seatPrice,
                rs.getLong(idColumnName));
    }
}
