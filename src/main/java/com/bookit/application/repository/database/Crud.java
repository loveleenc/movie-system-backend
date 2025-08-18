package com.bookit.application.repository.database;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface Crud {
    Object findById(Long id) throws DataAccessException;
    List<?> findAll()  throws DataAccessException;
//    void deleteById(Long id);
//    Object updateById(Object objectToUpdate);
}
