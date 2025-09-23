package com.bookit.application.persistence;

import com.bookit.application.security.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserDao{
    User findUserByUsername(String username) throws UsernameNotFoundException;

    Long createUser(User newUser);

    void deleteUser(String username);

    void updatePassword(String username, String newPassword);

    Integer findUserCountByUsernameOrEmail(String username, String email);

}
