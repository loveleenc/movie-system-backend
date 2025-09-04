package com.bookit.application.dao;

import com.bookit.application.entity.Ticket;
import com.bookit.application.dao.mappers.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
public class TicketDao implements Crud<Ticket> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private TicketMapper ticketMapper;

    public TicketDao(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Override
    public Ticket findById(Long id) throws DataAccessException {
        return null;
    }

    @Override
    public List<Ticket> findAll() throws DataAccessException {
        return List.of();
    }

    public List<Ticket> findTicketsByShow(String showId) throws DataAccessException{
        String sql = "SELECT T.id as ticketid, T.price, S.seatnumber, S.seattype, S.id as seatid, T.status FROM tickets T " +
                "JOIN seats S on T.seat = S.id " +
                "WHERE show = ?::uuid";
        return this.jdbcTemplate.query(sql, this.ticketMapper, showId);
    }

    public void createTickets(List<Ticket> tickets) throws DataAccessException{
        //TODO: batch update in multiples of 50-100
        String sql = "INSERT INTO tickets(seat, status, price, show) " +
                "VALUES(?, ?::ticketstatus, ?, ?::uuid)";
        List<Object[]> parameters = tickets.stream().map(ticket -> {
            return new Object[]{ticket.getSeat().getId(), ticket.getStatus(), ticket.getPrice(), ticket.getShow().getId()};
        }).toList();
        int[] argTypes = new int[4];
        argTypes[0] = Types.BIGINT;
        argTypes[1] = Types.VARCHAR;
        argTypes[2] = Types.BIGINT;
        argTypes[3] = Types.VARCHAR;

        this.jdbcTemplate.batchUpdate(sql, parameters, argTypes);

    }

    @Override
    public Long create(Ticket ticket) {
        //TODO: raise exception
        return (long)0;
    }
}


//    public Seat getSeat(String seatNumber, String theatreName) throws DataAccessException{
//        String sql = "select id, price FROM " +
//                "(select id, price from seats JOIN seatprices ON seats.theatre = seatprices.theatre AND seats.seattype = seatprices.seattype) as foo " +
//                "WHERE seatnumber = ? AND theatre = (SELECT id from theatre WHERE theatre.name = ?)";
//        return this.jdbcTemplate.queryForObject(sql, new SeatMapper(), seatNumber, theatreName);
//    }
//
//    public List<Ticket> getTicketsByShow(String showId){
//        String sql = "SELECT * FROM tickets T join seats S on T.seat = S.id WHERE show = ?::uuid";
//        return this.jdbcTemplate.query(sql, this.ticketsForSeatsMapper, showId);
//    }