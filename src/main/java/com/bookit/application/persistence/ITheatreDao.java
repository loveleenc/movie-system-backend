package com.bookit.application.persistence;

import com.bookit.application.entity.Theatre;
import com.bookit.application.common.ResourceNotFoundException;

import java.util.List;

public interface ITheatreDao   {
    List<Theatre> findAll(Long userId);
    Integer create(Theatre object);
    Theatre findById(Integer id, Long userId) throws ResourceNotFoundException;
}
