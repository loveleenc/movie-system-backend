package com.bookit.application.repository;

import com.bookit.application.entity.Show;
import com.bookit.application.entity.TheatreTimeSlots;
import com.bookit.application.repository.mappers.ShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShowDAO implements Crud<Show> {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ShowMapper showMapper;

    public ShowDAO(ShowMapper showMapper) {
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

    public List<TheatreTimeSlots> getBookedSlotsByTheatreId(Long theatreId) {
        String sql = "SELECT starttime, endtime FROM shows WHERE shows.theatre = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> new TheatreTimeSlots(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime()), theatreId);
    }

    public Long getShowInternalId(String showId) throws DataAccessException {
        String sql = "SELECT * FROM shows WHERE showid = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), showId);
    }

    @Override
    public Long create(Show show) throws DataAccessException {
        String sql = "INSERT INTO shows(theatre, starttime, endtime, movie, showlanguage) " +
                "VALUES(?, ?, ?, ?, ?)";
        return (long) this.jdbcTemplate.update(sql, show.getTheatreId(),
                show.getStartTime(),
                show.getEndTime(),
                show.getMovieId(),
                show.getLanguage());
//no show exists during this duration for the same theatre
//language should be from the movie release languages
//date of movie should be greater than or equal to movie release date
//endtime - startime >= movie duration
//        String sql3 = "INSERT INTO shows(theatre, starttime, endtime, movie, showlanguage) " +
//                "SELECT T.id, ?, ?, M.id, ? FROM " +
//                "(SELECT id FROM theatre WHERE theatre.theatrename = ?) T, " +
//                "(SELECT id, releasedate, language, duration FROM movies WHERE movies.name = ?) M " +
//                "WHERE EXISTS(SELECT 1 FROM movies WHERE ? = ANY(movies.language) AND " +
//                "movies.id = M.id and movies.releasedate <= ?::timestamp::date) AND " +
//                "((EXTRACT(EPOCH FROM timestamptz ?) - EXTRACT(EPOCH FROM timestamptz ?))/60 >= M.duration) AND  " +
//                "NOT EXISTS(SELECT 1 FROM shows WHERE shows.theatre = T.id AND " +
//                "(shows.starttime BETWEEN ? AND ?) AND " +
//                "(shows.endtime BETWEEN ? AND ?));";
//
//
//        String sql1 = "INSERT INTO shows(theatre, starttime, endtime, movie, showlanguage) " +
//                "SELECT T.id, '2028-01-01 16:00:00', '2028-01-01 18:30:00', M.id, 'English' FROM " +
//                "(SELECT id FROM theatre WHERE theatre.theatrename = 'Gunjan Cineplex') T, " +
//                "(SELECT id, releasedate, language, duration FROM movies WHERE movies.name = 'Inception') M " +
//                "WHERE EXISTS(SELECT 1 FROM movies WHERE 'English' = ANY(movies.language) AND  " +
//                "movies.id = M.id and movies.releasedate <= '2028-01-01 16:00:00'::timestamp::date) AND " +
//                "((EXTRACT(EPOCH FROM timestamptz '2028-01-01 18:30:00') - EXTRACT(EPOCH FROM timestamptz '2028-01-01 16:00:00'))/60 >= M.duration) AND  " +
//                "NOT EXISTS(SELECT 1 FROM shows WHERE shows.theatre = T.id AND " +
//                "(shows.starttime BETWEEN '2028-01-01 16:00:00' AND '2028-01-01 18:30:00') AND " +
//                "(shows.endtime BETWEEN '2028-01-01 16:00:00' AND '2028-01-01 18:30:00'));";
//
//
//        //start time
//        //endtime
//        //show language
//        //theatre name
//        //movie name
//        //show language (validation)
//        //start time date (validation)
//        // end time - start time >= movie duration (validation)
//        //starttime end time range 1 (validation)
//        //starttime end time range 2 (validation)
//
//
//        return (long) this.jdbcTemplate.update(sql3);
    }

    public List<Show> findShowsByMovie(String movieName, LocalDate movieReleaseDate) throws DataAccessException {
        String sql = "select * from shows S " +
                "INNER JOIN movies M ON S.movie = M.id " +
                "JOIN theatre T ON T.id = S.theatre " +
                "WHERE M.name = ? AND M.releasedate = ?";
        return this.jdbcTemplate.query(sql, this.showMapper, movieName, movieReleaseDate);
    }

    //    public Map<String, Long> getShowIdAndTheatre(String showId) throws DataAccessException {
//        String sql = "SELECT id, theatre FROM shows WHERE showid = ?";
//        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
//            Map<String, Long> idAndTheatre = new HashMap<>();
//            idAndTheatre.put("id", rs.getLong("id"));
//            idAndTheatre.put("theatre", (long) rs.getInt("theatre"));
//            return idAndTheatre;
//        }, showId);
//    }
}
