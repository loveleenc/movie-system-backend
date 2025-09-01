package com.bookit.application.repository;


import com.bookit.application.entity.Theatre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreDAO implements Crud<Theatre> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long findIdByExternalId(String externalId) {
        String sql = "SELECT id FROM theatre WHERE theatreid = ?::uuid";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), externalId);
    }

    @Override
    public Theatre findById(Long id) throws DataAccessException {
        return this.jdbcTemplate.queryForObject("SELECT * FROM theatre WHERE id = ?",
                (rs, rowNum) -> new Theatre(rs.getString("theatreName"), rs.getString("location"), rs.getLong("id")),
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
