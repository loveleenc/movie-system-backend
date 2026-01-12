package com.bookit.events.shows.db;

import com.bookit.events.shows.entity.Show;
import com.bookit.events.shows.entity.ShowTimeSlot;
import com.bookit.events.shows.entity.Theatre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ShowTheatreMapper implements RowMapper<Show> {
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Theatre theatre = new Theatre(rs.getString("theatrename"),
                rs.getString("location"),
                rs.getInt("theatreid"));
        UUID showId = UUID.fromString(rs.getString("id"));

        return new Show(timeSlot,
                theatre,
                null,
                rs.getString("showlanguage"),
                showId
        );
    }
}
