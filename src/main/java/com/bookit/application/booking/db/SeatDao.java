package com.bookit.application.booking.db;

import com.bookit.application.booking.entity.Seat;
import com.bookit.application.booking.entity.types.SeatCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Component
public class SeatDao implements ISeatDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Seat> getSeatPricesByTheatre(Integer theatreId) {
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


    @Override
    public void create(List<Seat> seats, Integer theatreId) {
        //TODO: batch update in multiples of 50-100
        //TODO: handle failures in creation of specific seats
        String sql = "INSERT INTO seats(seattype, theatre, seatnumber) VALUES(?::seatcategory, ?, ?)";
        List<Object[]> parameters = seats.stream().map(seat -> {
            return new Object[]{seat.getSeatType().code(), theatreId, seat.getSeatNumber()};
        }).toList();

        int[] argTypes = new int[3];
        argTypes[0] = Types.VARCHAR;
        argTypes[1] = Types.INTEGER;
        argTypes[2] = Types.VARCHAR;
        this.jdbcTemplate.batchUpdate(sql, parameters, argTypes);
    }

    @Override
    public void addPrices(Map<SeatCategory, Long> seatCategoryAndPrices, Integer theatreId) {
        String sql = "INSERT INTO seatprices(price, theatre, seattype) VALUES (?, ?, ?::seatcategory)";
        List<Object[]> parameters = seatCategoryAndPrices.keySet().stream().map(category ->
                new Object[]{seatCategoryAndPrices.get(category),
                        theatreId, category.code()}).toList();

        int[] argTypes = new int[3];
        argTypes[0] = Types.BIGINT;
        argTypes[1] = Types.INTEGER;
        argTypes[2] = Types.VARCHAR;
        this.jdbcTemplate.batchUpdate(sql, parameters, argTypes);
    }


}
