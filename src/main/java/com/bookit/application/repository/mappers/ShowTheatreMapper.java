package com.bookit.application.repository.mappers;

import com.bookit.application.entity.Show;
import com.bookit.application.entity.Theatre;
import com.bookit.application.entity.TheatreTimeSlots;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShowTheatreMapper implements RowMapper<Show> {
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        Theatre theatre = new Theatre(rs.getString("theatrename"),
                rs.getString("location"));
        TheatreTimeSlots timeSlot = new TheatreTimeSlots(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        return new Show(theatre, timeSlot,
                rs.getString("showlanguage"));
    }
}
