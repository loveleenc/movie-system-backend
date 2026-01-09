package com.bookit.application.showscheduling.db;

import com.bookit.application.showscheduling.entity.Show;
import com.bookit.application.showscheduling.entity.ShowTimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Component
public class ShowDao implements IShowDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ShowMapper showMapper;
    private ShowTheatreMapper showTheatreMapper;
    private ShowMovieMapper showMovieMapper;
    public ShowDao(ShowMapper showMapper, ShowTheatreMapper showTheatreMapper,
                   ShowMovieMapper showMovieMapper) {
        this.showMapper = showMapper;
        this.showTheatreMapper = showTheatreMapper;
        this.showMovieMapper = showMovieMapper;
    }

    @Override
    public Show findById(String id) throws DataAccessException {
        String sql = "SELECT T.id AS theatreid, M.id as movieid, * FROM shows S " +
                "JOIN movies M ON S.movie = M.id " +
                "JOIN theatre T ON S.theatre = T.id WHERE S.id = ?::uuid";
        return this.jdbcTemplate.queryForObject(sql, this.showMapper, id);
    }

    @Override
    public String create(Show show){
        String sql = "INSERT INTO shows(theatre, starttime, endtime, movie, showlanguage) " +
                "VALUES(?, ?, ?, ?, ?::movielanguage)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, show.getTheatreId());
            ps.setTimestamp(2, Timestamp.valueOf(show.getStartTime()));
            ps.setTimestamp(3, Timestamp.valueOf(show.getEndTime()));
            ps.setLong(4, show.getMovieId());
            ps.setString(5, show.getLanguage());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKeyList().get(0).get("id")).toString();
    }

    @Override
    public List<Show> findAll() throws DataAccessException {
        String message = "Method findAll for shows has not been implemented";
        throw new DataAccessException(message) {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    public List<Show> findShowsByTheatre(Integer theatreId, Long userId) throws DataAccessException{
      String sql = "SELECT DISTINCT " +
                    "M.name as moviename, " +
                   "S.starttime,  S.endtime, S.id as showid, S.showlanguage " +
                    "FROM shows S " +
                    "JOIN tickets T ON T.show = S.id " +
                    "JOIN movies M ON S.movie = M.id " +
                    "JOIN theatre T ON T.id = S.theatre " +
                   "WHERE S.theatre = ? AND S.starttime > NOW() AND T.status != 'cancelled'::ticketstatus AND T.owner = ? ORDER BY S.starttime";
        return this.jdbcTemplate.query(sql, this.showMovieMapper, theatreId, userId);
    }

    @Override
    public List<ShowTimeSlot> getBookedSlotsByTheatreId(Integer theatreId) {
        String sql = "SELECT starttime, endtime FROM shows WHERE shows.theatre = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> new ShowTimeSlot(rs.getTimestamp("starttime").toLocalDateTime(),
                rs.getTimestamp("endtime").toLocalDateTime()), theatreId);
    }

    @Override
    public List<Show> findShowsByMovie(Long movieId) throws DataAccessException {
        String sql = "select T.id AS theatreid, * from shows S  " +
                "JOIN theatre T ON T.id = S.theatre  " +
                "WHERE S.movie = ? AND S.starttime > NOW() ORDER BY T.location, S.theatre, S.showlanguage, S.starttime";
        return this.jdbcTemplate.query(sql, this.showTheatreMapper, movieId);
    }


//        return (long) this.jdbcTemplate.update(sql,
//                show.getTheatreId(),
//                show.getStartTime(),
//                show.getEndTime(),
//                show.getMovieId(),
//                show.getLanguage());
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
