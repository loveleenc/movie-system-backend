package com.bookit.application.security;

import com.bookit.application.persistence.IUserDao;
import com.bookit.application.security.entity.User;
import com.bookit.application.types.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserDao userDao;
    private PasswordEncoder passwordEncoder;
    public CustomUserDetailsService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userDao.findUserByUsername(username);
    }

    public User createUser(User user) throws UsernameOrEmailAlreadyExistsException{
        if(this.usernameOrEmailAlreadyExists(user.getUsername(), user.getEmail())){
            throw new UsernameOrEmailAlreadyExistsException("");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userDao.createUser(user);
        return this.userDao.findUserByUsername(user.getUsername());
    }

    public void updateUser(User user) {
        //TODO: implement
    }

    public void changePassword(String username, String newPassword) throws UsernameNotFoundException{
        if(!this.userExists(username)){
            throw new UsernameNotFoundException("Unable to change password as user with username does not exist");
        }
        this.userDao.updatePassword(username, newPassword);
    }

    private boolean userExists(String username){
        Integer count = this.userDao.findUserCountByUsernameOrEmail(username, "");
        return count > 0;
    }

    private Boolean usernameOrEmailAlreadyExists(String username, String email){
        Integer count = this.userDao.findUserCountByUsernameOrEmail(username, email);
        return count > 0;
    }

    public Long getCurrentUserId(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        return user.getId();
    }

    public Boolean activateUserAccount(String username) throws UsernameNotFoundException{
        return this.changeUserAccountStatus(username, AccountStatus.ACTIVE);
    }

    private Boolean changeUserAccountStatus(String username, AccountStatus status){
        User user = this.userDao.findUserByUsername(username);
        user.setAccountStatus(status);
        Integer numberOfUpdatedUsers = this.userDao.updateUserAccountStatus(user);
        return numberOfUpdatedUsers == 1;
    }
}
