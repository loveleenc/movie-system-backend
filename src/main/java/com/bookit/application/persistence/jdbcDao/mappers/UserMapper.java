package com.bookit.application.persistence.jdbcDao.mappers;

import com.bookit.application.security.entity.User;
import com.bookit.application.types.AccountStatus;
import com.bookit.application.types.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array roles = rs.getArray("roles");
        String[] roless = (String [])roles.getArray();
        User user = new User(rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                Arrays.stream(roless).map(Role::getRoleEnum).toList());
        user.setAccountStatus(Objects.requireNonNull(AccountStatus.getAccountStatusEnum(rs.getString("status"))));
        user.setId(rs.getLong("id"));
        return user;
    }
}
