package com.bookit.application.booking.db;

import com.bookit.application.booking.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
public class TicketDao implements ITicketDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private TicketSeatMapper ticketSeatMapper;
    private TicketMapper ticketMapper;

    public TicketDao(TicketSeatMapper ticketSeatMapper, TicketMapper ticketMapper) {
        this.ticketSeatMapper = ticketSeatMapper;
        this.ticketMapper = ticketMapper;
    }

    public List<Ticket> findTicketsByShow(String showId) throws DataAccessException {
        String sql = "SELECT owner, T.id as ticketid, T.price, S.seatnumber, S.seattype, S.id as seatid, T.status FROM tickets T " +
                "JOIN seats S on T.seat = S.id " +
                "WHERE show = ?::uuid";
        return this.jdbcTemplate.query(sql, this.ticketSeatMapper, showId);
    }

    public void createTickets(List<Ticket> tickets) throws DataAccessException {
        //TODO: batch update in multiples of 50-100
        String sql = "INSERT INTO tickets(seat, status, price, show) " +
                "VALUES(?, ?::ticketstatus, ?, ?::uuid)";
        List<Object[]> parameters = tickets.stream().map(ticket -> {
            return new Object[]{ticket.getSeat().getId(), ticket.getStatus().code(), ticket.getPrice(), ticket.getShow().getId()};
        }).toList();
        int[] argTypes = new int[4];
        argTypes[0] = Types.BIGINT;
        argTypes[1] = Types.VARCHAR;
        argTypes[2] = Types.BIGINT;
        argTypes[3] = Types.VARCHAR;

        this.jdbcTemplate.batchUpdate(sql, parameters, argTypes);

    }

    public void updateAllTicketsStatusForShow(String showId, String status) {
        String sql = "UPDATE tickets SET status = ?::ticketstatus WHERE show = ?::uuid";
        this.jdbcTemplate.update(sql, status, showId);
    }

    @Override
    public void bookOrCancelTickets(List<Ticket> tickets) {
        //TODO: how to recover if only some of the tickets get booked
        String sql = "UPDATE tickets SET owner = ?, status = ?::ticketstatus WHERE id = ?::uuid";
        List<Object[]> parameters = tickets.stream().map(ticket -> new Object[]{ticket.getOwnerId(), ticket.getStatus().code(), ticket.getId()}).toList();
        int[] argTypes = new int[3];
        argTypes[0] = Types.BIGINT;
        argTypes[1] = Types.VARCHAR;
        argTypes[2] = Types.VARCHAR;
        this.jdbcTemplate.batchUpdate(sql, parameters, argTypes);
    }

    @Override
    public Integer reserveOrReleaseTicket(Ticket ticket){
        String sql = "UPDATE tickets SET owner = ? , status = ?::ticketstatus WHERE id = ?::uuid";
        return this.jdbcTemplate.update(sql, ticket.getOwnerId(), ticket.getStatus().code(), ticket.getId());
    }

    @Override
    public List<Ticket> findTicketsByUser(Long userId) {
        String sql = "SELECT T.id as ticketid, T.price, T.status, T.show as showid, T.owner as ticketowner," +
                "S.seatnumber, S.seattype, S.id as seatid," +
                "SH.starttime, SH.endtime, SH.showlanguage, " +
                "TH.theatrename, TH.location, TH.id as theatreid," +
                "M.name " +
                "FROM tickets T " +
                "JOIN seats S ON T.seat = S.id " +
                "JOIN shows SH ON T.show = SH.id " +
                "JOIN theatre TH on SH.theatre = TH.id " +
                "JOIN movies M ON M.id = SH.movie " +
                "WHERE T.owner = ?";
        return this.jdbcTemplate.query(sql, this.ticketMapper, userId);
    }

    @Override
    public Ticket findById(String id) throws DataAccessException {
        String sql = "SELECT T.id as ticketid, T.price, T.status, T.show as showid, T.owner as ticketowner," +
                "S.seatnumber, S.seattype, S.id as seatid," +
                "SH.starttime, SH.endtime, SH.showlanguage, " +
                "TH.theatrename, TH.location, TH.id as theatreid," +
                "M.name " +
                "FROM tickets T " +
                "JOIN seats S ON T.seat = S.id " +
                "JOIN shows SH ON T.show = SH.id " +
                "JOIN theatre TH on SH.theatre = TH.id " +
                "JOIN movies M ON M.id = SH.movie " +
                "WHERE T.id = ?::uuid";
        return this.jdbcTemplate.queryForObject(sql, this.ticketMapper, id);
    }
}