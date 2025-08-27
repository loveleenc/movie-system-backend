package com.bookit.application.repository.mappers;

import com.bookit.application.entity.Show;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShowMapper implements RowMapper<Show> {
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        Show show = new Show();
        show.setTheatreName(rs.getString("theatrename"));
        show.setLanguage(rs.getString("showLanguage"));
        show.setStarttime(rs.getDate("starttime"), rs.getTime("starttime"));
        show.setEndTime(rs.getDate("endtime"), rs.getTime("endtime"));
        show.setMovieName(rs.getString("name"));
        show.setMovieReleaseDate(rs.getDate("releasedate").toLocalDate());
        show.setShowId(rs.getString("showid"));
        return show;
    }
}
