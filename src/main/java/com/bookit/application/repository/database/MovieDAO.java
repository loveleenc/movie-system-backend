package com.bookit.application.repository.database;

import com.bookit.application.entity.Movie;
import com.bookit.application.types.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MovieDAO implements Crud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MovieDAO() {
    }


    @Override
    public Movie findById(Long id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject("SELECT * FROM movies WHERE id = ?", Movie.class, id);
//        this.jdbcTemplate.query("SELECT * FROM movie WHERE id = ?", rowSet -> {
//            return new Movie(rowSet.getString("name"), rowSet.getInt("duration"), rowSet.getURL("image"));
//        }, id);
    }

    public List<Movie> findAll() throws DataAccessException {
        List<Movie> movies = new ArrayList<>();
        this.jdbcTemplate.query("SELECT * FROM movies",
                        (rs, rowNum) -> new Movie(rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getString("image"),
                                Stream.of((String[])rs.getArray("genre").getArray())
                                        .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                        .collect(Collectors.toList()),
                                rs.getString("releaseDate")))
                .forEach(movie -> movies.add(movie));
        return movies;
    }
}
