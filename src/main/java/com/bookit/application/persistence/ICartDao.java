package com.bookit.application.persistence;

import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import com.bookit.application.services.ResourceNotFoundException;

import java.lang.reflect.Field;
import java.util.List;

public interface ICartDao {
    Long add(Ticket ticket, Long userId);
    void remove(Long itemId);
    List<Item> get(Long userId);
    Item findById(Long itemId);
}
