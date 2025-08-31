package com.bookit.application.repository;

import com.bookit.application.entity.Movie;
import com.bookit.application.repository.mappers.MovieMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class MovieDAO implements Crud<Movie> {
    private JdbcTemplate jdbcTemplate;
    private MovieMapper movieMapper;

    public MovieDAO(JdbcTemplate jdbcTemplate, MovieMapper movieMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieMapper = movieMapper;
    }

    @Override
    public Movie findById(Long id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject("SELECT * FROM movies WHERE id = ?", this.movieMapper, id);
    }

    public List<Movie> findAll() throws DataAccessException {
        String sql = "SELECT * FROM movies";
        return this.jdbcTemplate.query(sql, this.movieMapper);
    }

    public List<Movie> findOngoingMovies() throws DataAccessException {
        String sql = "SELECT DISTINCT name, duration, image, genre, releasedate, language from movies M JOIN shows S ON M.id = S.movie WHERE M.releasedate >= ?";
        return this.jdbcTemplate.query(sql, this.movieMapper, OffsetDateTime.now().toLocalDate());
    }

    public List<Movie> findUpcomingMovies() throws DataAccessException {
        String sql = "SELECT name, duration, image, genre, releasedate, language FROM movies M WHERE M.id NOT IN (SELECT movie FROM shows) AND M.releasedate >= ?";
        return this.jdbcTemplate.query(sql, this.movieMapper, OffsetDateTime.now().toLocalDate());
    }

    public List<Movie> filterMovies(List<String> genre, List<String> languages, LocalDate releasedOnOrAfter) throws DataAccessException {
        String[] genreArray = genre.toArray(new String[0]);
        String[] languagesArray = languages.toArray(new String[0]);

        String sql = "SELECT * FROM movies WHERE " +
                "(CARDINALITY(?::moviegenre[]) = 0 OR movies.genre && ?::moviegenre[]) " +
                "AND " +
                "(CARDINALITY(?::movielanguage[]) = 0 OR movies.language && ?::movielanguage[])" +
                "AND " +
                "releasedate >= ?";
        return this.jdbcTemplate.query(sql, this.movieMapper,
                genreArray,
                genreArray,
                languagesArray,
                languagesArray,
                releasedOnOrAfter);
    }

    public Long create(Movie movie) throws DataAccessException, NullPointerException {
        String sql = "INSERT INTO movies(name, duration, image, genre, releaseDate, language) " +
                "VALUES(?, ?, ?, ?::moviegenre[], ?, ?::movielanguage[]) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, movie.getName());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getPoster());
            Array genreArray = connection.createArrayOf("moviegenre", movie.getGenreList().toArray());
            ps.setArray(4, genreArray);
            ps.setDate(5, Date.valueOf(movie.getReleaseDate()));
            Array languageArray = connection.createArrayOf("movielanguage", movie.getLanguages().toArray());
            ps.setArray(6, languageArray);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteMovie(Movie movie) throws DataAccessException {
        String sql = "DELETE FROM movies WHERE name = ? AND duration = ? AND releasedate = ?";
        this.jdbcTemplate.update(sql,
                movie.getName(),
                movie.getDuration(),
                movie.getReleaseDate());
    }
}


