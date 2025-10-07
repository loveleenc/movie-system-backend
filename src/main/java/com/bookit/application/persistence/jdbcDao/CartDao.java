package com.bookit.application.persistence.jdbcDao;

import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ICartDao;

import java.util.List;

public class CartDao implements ICartDao {
    //TODO: implement all methods

    @Override
    public Long add(Ticket ticket, Long userId) {
        return 0L;
    }

    @Override
    public Long remove(Long itemId) {
        return 0L;
    }

    @Override
    public List<Item> get(Long userId) {
        return List.of();
    }

    @Override
    public Item findById(Long itemId) {
        return null;
    }
}
