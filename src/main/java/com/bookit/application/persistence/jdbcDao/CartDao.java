package com.bookit.application.persistence.jdbcDao;

import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ICartDao;
import com.bookit.application.persistence.ITicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class CartDao implements ICartDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ITicketDao ticketDao;


    public CartDao(ITicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    private Long getCartId(Long userId){
        String sql = "SELECT id FROM cart WHERE owner = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), userId);
    }

    @Override
    public Long add(Ticket ticket, Long userId) {
        Long cartId = this.getCartId(userId);
        String sql = "INSERT INTO cartdetails(cartid, ticket) VALUES(?, ?::uuid) RETURNING itemid";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, cartId);
            ps.setString(2, ticket.getId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void remove(Long itemId) {
        String sql = "DELETE FROM cartdetails WHERE itemid = ?";
        this.jdbcTemplate.update(sql, itemId);
    }

    @Override
    public List<Item> get(Long userId) {
        Long cartId = this.getCartId(userId);
        String sql = "SELECT * FROM cartdetails WHERE cartid = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            String ticketId = rs.getString("ticket");
            Long itemId = rs.getLong("itemid");
            Ticket ticket = this.ticketDao.findById(ticketId);
            return new Item(ticket, itemId);
        }, cartId);
    }

    @Override
    public Item findById(Long itemId) {
        String sql = "SELECT * FROM cartdetails WHERE itemid = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            String ticketId = rs.getString("ticket");
            Ticket ticket = this.ticketDao.findById(ticketId);
            return new Item(ticket, itemId);
        }, itemId);
    }
}
