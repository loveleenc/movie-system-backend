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
public class ShowMovieMapper implements RowMapper<Show> {
    private MovieMapper movieMapper;
    public ShowMovieMapper(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShowTimeSlot timeSlot = new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime());
        Movie movie = this.movieMapper.getMovieName(rs, "moviename");
        Theatre theatre =null;
        UUID showId = UUID.fromString(rs.getString("showid"));
        return new Show(timeSlot,
                theatre,
                movie,
                rs.getString("showlanguage"),
                showId
        );
    }
}
