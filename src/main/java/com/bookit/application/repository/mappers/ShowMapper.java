package com.bookit.application.repository.mappers;

import com.bookit.application.entity.Show;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowMapper implements RowMapper<Show> {
    @Override
    public Show mapRow(ResultSet rs, int rowNum) throws SQLException {
        Show show = new Show();
        show.setTheatreName(rs.getString("theatrename"));
        show.setLanguage(rs.getString("showLanguage"));
        show.setStarttime(rs.getTime("starttime"));
        show.setEndTime(rs.getTime("endtime"));
        show.setMovieName(rs.getString("name"));
        show.setMovieReleaseDate(rs.getDate("releasedate").toLocalDate());
        return show;
    }
}
