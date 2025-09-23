package com.bookit.application.persistence;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface Crud<T, idType> {
    T findById(idType id) throws DataAccessException;

    List<T> findAll()  throws DataAccessException;

    idType create(T object);

//    void deleteById(Long id);
//    Object updateById(Object objectToUpdate);
}
