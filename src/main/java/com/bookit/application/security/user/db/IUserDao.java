package com.bookit.application.security.user.db;

import com.bookit.application.security.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDao{
    User findUserByUsername(String username) throws UsernameNotFoundException;

    Long createUser(User newUser);

    void deleteUser(String username);

    void updatePassword(String username, String newPassword);

    Integer findUserCountByUsernameOrEmail(String username, String email);

    Integer updateUserAccountStatus(User user);
}
