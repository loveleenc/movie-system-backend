package com.bookit.application.repository.database;

import com.bookit.application.entity.Movie;
import com.bookit.application.types.MovieGenre;
import com.bookit.application.types.MovieLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.sql.Array;

@Component
public class MovieDAO implements Crud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MovieDAO() {
    }


    @Override
    public Movie findById(Long id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject("SELECT * FROM movies WHERE id = ?", Movie.class, id);
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
                                rs.getString("releaseDate"),
                                Stream.of((String[])rs.getArray("language").getArray())
                                        .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                        .collect(Collectors.toList())
                                ))
                .forEach(movie -> movies.add(movie));
        return movies;
    }

    public List<Movie> findMoviesWithActiveTickets() throws DataAccessException{
        List<Movie> movies = new ArrayList<>();

        this.jdbcTemplate.query("SELECT * FROM movies WHERE id = ANY (SELECT movie FROM tickets WHERE tickets.movie = movies.id AND ? >= movies.releasedate AND tickets.status = 'available')",
                (rs, rowNum) -> new Movie(rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("image"),
                        Stream.of((String[])rs.getArray("genre").getArray())
                                .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList()),
                        rs.getString("releaseDate"),
                        Stream.of((String[])rs.getArray("language").getArray())
                                .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList())
                ), OffsetDateTime.now()).forEach(movie -> movies.add(movie));
        return movies;
    }

    public List<Movie> findUpcomingMovies() throws DataAccessException {
        List<Movie> movies = new ArrayList<>();

        this.jdbcTemplate.query("SELECT * FROM movies WHERE id = ANY (SELECT movie FROM tickets WHERE tickets.movie = movies.id AND ? <= movies.releasedate)",
                (rs, rowNum) -> new Movie(rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("image"),
                        Stream.of((String[])rs.getArray("genre").getArray())
                                .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList()),
                        rs.getString("releaseDate"),
                        Stream.of((String[])rs.getArray("language").getArray())
                                .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList())
                ), OffsetDateTime.now()).forEach(movie -> movies.add(movie));
        return movies;
    }
//LocalDate releasedOnOrAfterDate

    public List<Movie> filterMovies(List<String> genre, List<String> languages) throws DataAccessException{
        List<Movie> movies = new ArrayList<>();
        String[] genreArray = genre.toArray(new String[0]);
        String[] languagesArray = languages.toArray(new String[0]);

        String sql = "SELECT * FROM movies WHERE " +
                "(CARDINALITY(?::moviegenre[]) = 0 OR movies.genre && ?::moviegenre[]) " +
                "AND " +
                "(CARDINALITY(?::movielanguage[]) = 0 OR movies.language && ?::movielanguage[])";
        this.jdbcTemplate.query(sql, (rs, rowNum) -> new Movie(rs.getString("name"),
                rs.getInt("duration"),
                rs.getString("image"),
                Stream.of((String[])rs.getArray("genre").getArray())
                        .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                        .collect(Collectors.toList()),
                rs.getString("releaseDate"),
                Stream.of((String[])rs.getArray("language").getArray())
                        .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                        .collect(Collectors.toList())
        ), genreArray, genreArray, languagesArray, languagesArray).forEach(movie -> movies.add(movie));

        return movies;
    }
}


