package com.bookit.application.persistence.jdbcDao.mappers;

import com.bookit.application.entity.Theatre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TheatreMapper implements RowMapper<Theatre> {
    @Override
    public Theatre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.mapTheatreRow(rs, rowNum, "id");
    }

    public Theatre mapTheatreRow(ResultSet rs, int rowNum, String idColumnAlias) throws SQLException {
        Theatre theatre = new Theatre(rs.getString("theatreName"), rs.getString("location"), rs.getInt(idColumnAlias));
        theatre.setOwnerId(rs.getLong("owner"));
        return theatre;
    }
}
