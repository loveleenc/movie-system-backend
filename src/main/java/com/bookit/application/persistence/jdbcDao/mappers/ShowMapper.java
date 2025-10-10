package com.bookit.application.persistence.jdbcDao.mappers;

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
    private TheatreMapper theatreMapper;
    public ShowMapper(MovieMapper movieMapper, TheatreMapper theatreMapper) {
        this.movieMapper = movieMapper;
        this.theatreMapper = theatreMapper;
    }

    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Movie movie = this.movieMapper.getMovie(rs, "movieid");
        Theatre theatre = this.theatreMapper.mapTheatreRow(rs, rowNum, "theatreid");
        UUID showId = UUID.fromString(rs.getString("id"));
        return new Show(timeSlot,
                theatre,
                movie,
                rs.getString("showlanguage"),
                showId
        );
    }
}
