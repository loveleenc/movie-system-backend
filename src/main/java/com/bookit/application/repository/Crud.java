package com.bookit.application.repository;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface Crud<T> {
    Object findById(Long id) throws DataAccessException;
    List<?> findAll()  throws DataAccessException;
    Integer create(T object);
//    void deleteById(Long id);
//    Object updateById(Object objectToUpdate);
}
