package com.bookit.application.persistence.jdbcDao;

import com.bookit.application.entity.Seat;
import com.bookit.application.persistence.ISeatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SeatDao implements ISeatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
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
