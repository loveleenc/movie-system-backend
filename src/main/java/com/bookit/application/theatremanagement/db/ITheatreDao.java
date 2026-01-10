package com.bookit.application.theatremanagement.db;

import com.bookit.application.theatremanagement.entity.Theatre;
import com.bookit.application.theatremanagement.ResourceNotFoundException;

import java.util.List;

public interface ITheatreDao   {
    List<Theatre> findAll(Long userId);
    Integer create(Theatre object);
    Theatre findById(Integer id, Long userId) throws ResourceNotFoundException;
}
