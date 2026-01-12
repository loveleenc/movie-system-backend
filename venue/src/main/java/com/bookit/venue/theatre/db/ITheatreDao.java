package com.bookit.venue.theatre.db;

import com.bookit.venue.theatre.entity.Theatre;
import com.bookit.venue.theatre.ResourceNotFoundException;

import java.util.List;

public interface ITheatreDao   {
    List<Theatre> findAll(Long userId);
    Integer create(Theatre object);
    Theatre findById(Integer id, Long userId) throws ResourceNotFoundException;
}
