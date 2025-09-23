package com.bookit.application.persistence.jdbcDao;


import com.bookit.application.entity.Theatre;
import com.bookit.application.persistence.Crud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreDao implements Crud<Theatre, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Theatre findById(Long id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject("SELECT * FROM theatre WHERE id = ?",
                (rs, rowNum) -> {
                    return new Theatre(rs.getString("theatreName"), rs.getString("location"), rs.getLong("id"));
                },
                id);
    }

    @Override
    public List<?> findAll() throws DataAccessException {
        return List.of();
    }

    @Override
    public Long create(Theatre object) {
        return 0L;
    }

}
