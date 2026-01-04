package com.bookit.application.theatremanagement.db;


import com.bookit.application.theatremanagement.entity.Theatre;
import com.bookit.application.common.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class TheatreDao implements ITheatreDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private TheatreMapper theatreMapper;

    public TheatreDao(TheatreMapper theatreMapper) {
        this.theatreMapper = theatreMapper;
    }

    @Override
    public Theatre findById(Integer id, Long userId) throws DataAccessException, ResourceNotFoundException {
        try{
            return this.jdbcTemplate.queryForObject("SELECT * FROM theatre WHERE id = ? AND owner = ?",
                    this.theatreMapper,
                    id, userId);
        }
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(String.format("A single result was not returned when searching for theatre by id. Actual result size: %s", e.getActualSize()),
                    e);
        }
    }

    @Override
    public List<Theatre> findAll(Long userId) throws DataAccessException {
        String sql = "SELECT * FROM theatre WHERE owner = ?";
        return this.jdbcTemplate.query(sql, this.theatreMapper, userId);
    }

    @Override
    public Integer create(Theatre theatre) {
        String sql = "INSERT INTO theatre(theatrename, location, owner) VALUES(?, ?, ?) RETURNING id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theatre.getName());
            ps.setString(2, theatre.getLocation());
            ps.setLong(3, theatre.getOwnerId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

}
