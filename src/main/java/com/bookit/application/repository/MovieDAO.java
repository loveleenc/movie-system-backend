package com.bookit.application.repository;

import com.bookit.application.entity.Movie;
import com.bookit.application.types.MovieGenre;
import com.bookit.application.types.MovieLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
    }

    public List<Movie> findAll() throws DataAccessException {
        List<Movie> movies = new ArrayList<>();
        this.jdbcTemplate.query("SELECT * FROM movies",
                        (rs, rowNum) -> new Movie(rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getString("image"),
                                Stream.of((String[]) rs.getArray("genre").getArray())
                                        .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                        .collect(Collectors.toList()),
                                rs.getString("releaseDate"),
                                Stream.of((String[]) rs.getArray("language").getArray())
                                        .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                        .collect(Collectors.toList())
                        ))
                .forEach(movie -> movies.add(movie));
        return movies;
    }

    public List<Movie> findMoviesWithActiveTickets() throws DataAccessException {
        List<Movie> movies = new ArrayList<>();

        this.jdbcTemplate.query("SELECT * FROM movies WHERE id = ANY (SELECT movie FROM tickets WHERE tickets.movie = movies.id AND ? >= movies.releasedate AND tickets.status = 'available')",
                (rs, rowNum) -> new Movie(rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("image"),
                        Stream.of((String[]) rs.getArray("genre").getArray())
                                .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList()),
                        rs.getString("releaseDate"),
                        Stream.of((String[]) rs.getArray("language").getArray())
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
                        Stream.of((String[]) rs.getArray("genre").getArray())
                                .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList()),
                        rs.getString("releaseDate"),
                        Stream.of((String[]) rs.getArray("language").getArray())
                                .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList())
                ), OffsetDateTime.now()).forEach(movie -> movies.add(movie));
        return movies;
    }
//LocalDate releasedOnOrAfterDate

    public List<Movie> filterMovies(List<String> genre, List<String> languages, LocalDate releasedOnOrAfter) throws DataAccessException {
        List<Movie> movies = new ArrayList<>();
        String[] genreArray = genre.toArray(new String[0]);
        String[] languagesArray = languages.toArray(new String[0]);

        String sql = "SELECT * FROM movies WHERE " +
                "(CARDINALITY(?::moviegenre[]) = 0 OR movies.genre && ?::moviegenre[]) " +
                "AND " +
                "(CARDINALITY(?::movielanguage[]) = 0 OR movies.language && ?::movielanguage[])" +
                "AND " +
                "releasedate >= ?";
        this.jdbcTemplate.query(sql, (rs, rowNum) -> new Movie(rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("image"),
                        Stream.of((String[]) rs.getArray("genre").getArray())
                                .map(arrayElement -> MovieGenre.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList()),
                        rs.getString("releaseDate"),
                        Stream.of((String[]) rs.getArray("language").getArray())
                                .map(arrayElement -> MovieLanguage.valueOf(arrayElement.toUpperCase()).getCode())
                                .collect(Collectors.toList())
                ), genreArray,
                genreArray,
                languagesArray,
                languagesArray,
                releasedOnOrAfter).forEach(movie -> movies.add(movie));

        return movies;
    }

    public Integer createNewMovie(Movie movie) throws DataAccessException{
        String sql = "INSERT INTO movies(name, duration, image, genre, releaseDate, language) " +
                "VALUES(?, ?, ?, ?::moviegenre[], ?, ?::movielanguage[])";

        return this.jdbcTemplate.update(sql,
                movie.getName(),
                movie.getDuration(),
                movie.getPoster(),
                movie.getGenreList().toArray(new String[0]),
                movie.getReleaseDate(),
                movie.getLanguages().toArray(new String[0]));
    }
}


