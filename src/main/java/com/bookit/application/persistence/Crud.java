package com.bookit.application.persistence;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface Crud<T> {
    T findById(Long id) throws DataAccessException;

    List<?> findAll()  throws DataAccessException;
    Long create(T object);
//    void deleteById(Long id);
//    Object updateById(Object objectToUpdate);
}
