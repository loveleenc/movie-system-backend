package com.bookit.application.repository;

import com.bookit.application.entity.Show;
import com.bookit.application.repository.mappers.ShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ShowDAO implements Crud<Show> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ShowMapper showMapper;

    public ShowDAO(ShowMapper showMapper){
        this.showMapper = showMapper;
    }

    @Override
    public Show findById(Long id) throws DataAccessException {
        return null;
    }

    @Override
    public List<Show> findAll() throws DataAccessException {
        return List.of();
    }
//    public Integer findMovieIdByNameAndReleaseDate(String name, LocalDate releaseDate){
//        String sql = "SELECT id FROM movies WHERE name = ? AND releasedate = ?";
//        return this.jdbcTemplate.queryForObject(sql, Integer.class, name, releaseDate);
//    }
//
//    public Theatre findTheatreByName(String theatreName){
//        String sql = "SELECT * FROM movies WHERE name = ?";
//        return this.jdbcTemplate.queryForObject(sql, this.theatreMapper, theatreName);
//    }

    @Override
    public Long create(Show show) throws DataAccessException {
        String sql = "INSERT INTO shows(theatre, starttime, endtime, movie, showlanguage) " +
                "SELECT T.id, ?, ?, M.id, ? FROM " +
                "(SELECT id FROM theatre WHERE theatre.theatrename = ?) T, " +
                "(SELECT id, releasedate, language, duration FROM movies WHERE movies.name = ?) M " +
                "WHERE EXISTS(SELECT 1 FROM movies WHERE ? = ANY(movies.language) AND " +
                "movies.id = M.id and movies.releasedate <= ?::timestamp::date) AND " +
                "((EXTRACT(EPOCH FROM timestamptz ?) - EXTRACT(EPOCH FROM timestamptz ?))/60 >= M.duration) AND  " +
                "NOT EXISTS(SELECT 1 FROM shows WHERE shows.theatre = T.id AND " +
                "(shows.starttime BETWEEN ? AND ?) AND " +
                "(shows.endtime BETWEEN ? AND ?));";
        return (long) this.jdbcTemplate.update(sql,
                show.getStarttime(),
                show.getEndTime(),
                show.getLanguage(),
                show.getTheatreName(),
                show.getMovieName(),
                show.getLanguage(),
                show.getStarttime(),
                show.getEndTime(),
                show.getStarttime(),
                show.getStarttime(),
                show.getEndTime(),
                show.getStarttime(),
                show.getEndTime());
    }

    public List<Show> findShowsByMovie(String movieName, LocalDate movieReleaseDate) throws DataAccessException{
        String sql = "select * from shows S " +
                "INNER JOIN movies M ON S.movie = M.id " +
                "JOIN theatre T ON T.id = S.theatre " +
                "WHERE M.name = ? AND M.releasedate = ?";
        return this.jdbcTemplate.query(sql, this.showMapper, movieName, movieReleaseDate);
    }
}
