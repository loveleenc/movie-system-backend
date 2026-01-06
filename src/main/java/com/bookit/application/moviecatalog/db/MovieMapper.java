package com.bookit.application.moviecatalog.db;

import com.bookit.application.moviecatalog.entity.Movie;
import com.bookit.application.moviecatalog.entity.MovieBuilder;
import com.bookit.application.moviecatalog.entity.types.MovieGenre;
import com.bookit.application.moviecatalog.entity.types.MovieLanguage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MovieMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.getMovie(rs, "id");
    }

    public Movie getMovieName(ResultSet rs, String nameColumnAlias) throws SQLException{
        return new MovieBuilder().setName(rs.getString("moviename")).build();
    }

    public Movie getMovie(ResultSet rs, String idColumnName) throws SQLException{
        return new Movie(rs.getString("name"),
                rs.getInt("duration"),
                rs.getString("image"),
                Stream.of((String[]) rs.getArray("genre").getArray())
                        .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase().replace("-", "_")).code())
                        .collect(Collectors.toList()),
                rs.getDate("releaseDate").toLocalDate(),
                Stream.of((String[]) rs.getArray("language").getArray())
                        .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).code())
                        .collect(Collectors.toList()),
                rs.getLong(idColumnName)
        );
    }
}
