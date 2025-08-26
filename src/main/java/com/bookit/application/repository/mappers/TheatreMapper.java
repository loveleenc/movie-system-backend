package com.bookit.application.repository.mappers;

import com.bookit.application.entity.Theatre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TheatreMapper implements RowMapper<Theatre> {
    @Override
    public Theatre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Theatre(rs.getString("theatreName"), rs.getString("location"), rs.getLong("id"));
    }
}


