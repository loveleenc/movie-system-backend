package com.bookit.application.persistence;

import com.bookit.application.entity.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SeatDao implements Crud<Seat> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Seat findById(Long id) throws DataAccessException {
        return null;
    }

    @Override
    public List<?> findAll() throws DataAccessException {
        return List.of();
    }

    @Override
    public Long create(Seat object) {
        return 0L;
    }

    public List<Integer> findIdByTheatre(Long theatreId){
        String sql = "SELECT id FROM seats WHERE theatre = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), theatreId);
    }

    public List<Seat> getSeatPricesByTheatre(Long theatreId){
        String sql = "SELECT S.id, SP.price, SP.seattype, S.seatnumber FROM seats S " +
                "JOIN seatprices SP ON S.seattype = SP.seattype AND S.theatre = SP.theatre " +
                "WHERE S.theatre = ? " +
                "ORDER BY S.seattype";
        return this.jdbcTemplate.query(sql,
                (rs, rowNum) ->
                        new Seat(rs.getString("seatnumber"),
                                rs.getString("seattype"),
                                rs.getLong("price"),
                                rs.getLong("id")),
                theatreId);
    }

}
