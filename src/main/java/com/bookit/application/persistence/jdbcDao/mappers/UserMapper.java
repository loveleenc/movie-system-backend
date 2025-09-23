package com.bookit.application.persistence.jdbcDao.mappers;

import com.bookit.application.security.entity.Role;
import com.bookit.application.security.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                Stream.of(rs.getArray("roles")).map(role -> new Role(role.toString())).toList());
    }
}
