package com.bookit.application.booking.db;

import com.bookit.application.booking.entity.Item;
import com.bookit.application.booking.entity.Ticket;

import java.util.List;

public interface ICartDao {
    Long add(Ticket ticket, Long userId);
    void remove(Long itemId);
    List<Item> get(Long userId);
    Item findById(Long itemId);
    void createCart(Long userId);
    void extendCartExpiry(Long userId);
    Integer getItemCount(Long userId);
}
