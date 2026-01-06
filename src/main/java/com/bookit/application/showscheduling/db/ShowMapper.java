package com.bookit.application.showscheduling.db;

import com.bookit.application.showscheduling.entity.Movie;
import com.bookit.application.showscheduling.entity.Show;
import com.bookit.application.showscheduling.entity.ShowTimeSlot;
import com.bookit.application.showscheduling.entity.Theatre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ShowMapper implements RowMapper<Show> {

    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Theatre theatre = new Theatre(rs.getString("theatreName"), rs.getString("location"), rs.getInt("theatreid"));

        UUID showId = UUID.fromString(rs.getString("id"));
        Movie movie = null;
        return new Show(timeSlot,
                theatre,
                movie,  //populated using the movie fetched in the service
                rs.getString("showlanguage"),
                showId
        );
    }
}
