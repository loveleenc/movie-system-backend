package com.bookit.application.persistence.jdbcDao;

import com.bookit.application.entity.Cart;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    private Cart getCart(Long userId){
        String sql = "SELECT * FROM cart WHERE owner = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Cart(rs.getLong("id"), rs.getTimestamp("expiry").toLocalDateTime()), userId);
    }

    @Override
    public Long add(Ticket ticket, Long userId) {
        Cart cart = this.getCart(userId);
        String sql = "INSERT INTO cartdetails(cartid, ticket) VALUES(?, ?::uuid) RETURNING itemid";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, cart.getCartId());
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
        Cart cart = this.getCart(userId);
        String sql = "SELECT * FROM cartdetails WHERE cartid = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            String ticketId = rs.getString("ticket");
            Long itemId = rs.getLong("itemid");
            Ticket ticket = this.ticketDao.findById(ticketId);
            return new Item(ticket, itemId);
        }, cart.getCartId());
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

    @Override
    public void createCart(Long userId){
        String sql = "INSERT INTO cart(owner, expiry) VALUES(?, ?)";
        this.jdbcTemplate.update(sql, userId, null);
    }

    @Override
    public void extendCartExpiry(Long userId) {
        Cart cart = this.getCart(userId);
        cart.extendExpiry();
        String sql = "UPDATE cart SET expiry = ? WHERE id = ?";
        this.jdbcTemplate.update(sql, Timestamp.valueOf(cart.getExpiry()), cart.getCartId());
    }

    @Override
    public Integer getItemCount(Long userId){
        String sql = "SELECT COUNT(*) FROM cartdetails CD JOIN cart C ON CD.cartid = C.id WHERE C.owner = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("count"), userId);
    }

//    CREATE OR REPLACE FUNCTION get_tickets_to_release()
//    RETURNS TABLE(id uuid) AS $$
//    BEGIN
//    RETURN QUERY
//    SELECT T.id FROM tickets T JOIN cartdetails CD ON T.id = CD.ticket
//    JOIN cart C ON C.id = CD.cartid
//    WHERE C.expiry <= now();
//    END;
//    $$ LANGUAGE plpgsql;

//    CREATE PROCEDURE update_all_carts()
//    LANGUAGE plpgsql AS $$
//    BEGIN
//    UPDATE tickets SET status = 'available' WHERE tickets.id IN (SELECT id FROM get_tickets_to_release());
//    DELETE FROM cartdetails USING cart WHERE cartdetails.cartid = cart.id AND cart.expiry <= now();
//    END;
//    $$;

    //SELECT cron.schedule('* * * * *', 'CALL update_all_carts();');



//    private void createNewExpiry(Cart cart){
//        LocalDateTime newExpiry = cart.createNewCartExpiry();
//        String expiry = String.format("%d %d * * *", newExpiry.getMinute(), newExpiry.getHour());
//        //also add the part where ticket ownership is removed and ticket status changes back to available
//        String sql = "SELECT cron.schedule(?, " +
//                "$$" +
//                "DELETE FROM cartdetails WHERE cartid = ?;" +
//                "$$" +
//                ") RETURNING jobid";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        this.jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"jobid"});
//            ps.setString(1, expiry);
//            ps.setLong(2, cart.getCartId());
//            return ps;
//        }, keyHolder);
////        cart.setCartExpirySchedulerId(Objects.requireNonNull(keyHolder.getKey()).longValue());
//        String addNewExpirySql = "UPDATE cart SET expiryjobid = ? WHERE id = ?";
//        this.jdbcTemplate.update(addNewExpirySql, cart.getExpiry(), cart.getCartId());
//    }
//
//    private void extendCurrentExpiry(Cart cart){
//        LocalDateTime newExpiry = cart.createNewCartExpiry();
//        String expiry = String.format("%d %d * * *", newExpiry.getMinute(), newExpiry.getHour());
//        String sql = "UPDATE cron.job SET schedule = ? WHERE jobid = ?";
//        this.jdbcTemplate.update(sql, expiry, cart.getExpiry());
//    }


}
