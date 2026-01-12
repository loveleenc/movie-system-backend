package com.bookit.events.shows.db;

import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.entity.Show;
import com.bookit.events.shows.entity.ShowTimeSlot;
import com.bookit.events.shows.entity.Theatre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ShowMovieMapper implements RowMapper<Show> {

    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Theatre theatre = null;
        UUID showId = UUID.fromString(rs.getString("showid"));
        Movie movie = new Movie(rs.getString("moviename"),
                null,
                null,
                null,
                null,
                null,
                null);
        return new Show(timeSlot,
                theatre,
                movie,
                rs.getString("showlanguage"),
                showId
        );
    }
}
