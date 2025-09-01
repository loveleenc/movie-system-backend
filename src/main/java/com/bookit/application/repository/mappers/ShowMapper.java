package com.bookit.application.repository.mappers;

import com.bookit.application.entity.Show;
import com.bookit.application.entity.TheatreTimeSlots;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShowMapper implements RowMapper<Show> {
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        TheatreTimeSlots timeSlot = new TheatreTimeSlots(rs.getTimestamp("starttime").toLocalDateTime(), rs.getTimestamp("endtime").toLocalDateTime());
        return new Show(rs.getLong("movie"),
            rs.getLong("theatre"),
            rs.getString("showlanguage"),
            timeSlot
        );
    }
}
