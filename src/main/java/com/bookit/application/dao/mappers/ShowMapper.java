package com.bookit.application.dao.mappers;

import com.bookit.application.entity.Movie;
import com.bookit.application.entity.Show;
import com.bookit.application.entity.ShowTimeSlot;
import com.bookit.application.entity.Theatre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ShowMapper implements RowMapper<Show> {
    private MovieMapper movieMapper;

    public ShowMapper(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Movie movie = this.movieMapper.getMovie(rs, "movieid");
        Theatre theatre = new Theatre(rs.getString("theatrename"),
                rs.getString("location"),
                rs.getLong("theatreid"));
        UUID showId = UUID.fromString(rs.getString("id"));
        return new Show(timeSlot,
                theatre,
                movie,
                rs.getString("showlanguage"),
                showId
        );
    }
}
