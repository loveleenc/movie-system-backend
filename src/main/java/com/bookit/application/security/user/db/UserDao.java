package com.bookit.application.security.user.db;

import com.bookit.application.security.entity.User;
import com.bookit.application.security.entity.types.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.Objects;

@Component
public class UserDao implements IUserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper;

    public UserDao(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public User findUserByUsername(String username) throws DataAccessException, UsernameNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return this.jdbcTemplate.queryForObject(sql, this.userMapper, username);
        }
        catch (IncorrectResultSizeDataAccessException e){
            throw new UsernameNotFoundException(String.format("User with username '%s' is not found", username));
        }
    }

    public Integer findUserCountByUsernameOrEmail(String username, String email){
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        return this.jdbcTemplate.queryForObject(sql, Integer.class, username, email);
    }

    @Override
    public Integer updateUserAccountStatus(User user) {
        String sql = "UPDATE users SET status = ?::accountstatus WHERE id = ?";
        return this.jdbcTemplate.update(sql, user.getAccountStatus().code(), user.getId());
    }

    @Override
    public Long createUser(User newUser) {
        String sql = "INSERT INTO users(firstname, lastname, email, password, username, roles, status) " +
                "VALUES(?, ?, ?, ?, ?, ?::role[], ?::accountstatus) RETURNING id";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, newUser.getFirstName());
            ps.setString(2, newUser.getLastName());
            ps.setString(3, newUser.getEmail());
            ps.setString(4, newUser.getPassword());
            ps.setString(5, newUser.getUsername());
            Array rolesArray = connection.createArrayOf("role", newUser.getRoles().stream().map(Role::code).toArray());
            ps.setArray(6, rolesArray);
            ps.setString(7, newUser.getAccountStatus().code());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unable to delete user. This functionality is not yet supported");
//        String sql = "DELETE FROM users WHERE username = ?";
//        this.jdbcTemplate.update(sql, username);
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        this.jdbcTemplate.update(sql, newPassword, username);
    }


}
