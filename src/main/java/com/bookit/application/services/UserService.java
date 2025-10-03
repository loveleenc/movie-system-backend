package com.bookit.application.services;

import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.security.UsernameOrEmailAlreadyExistsException;
import com.bookit.application.security.entity.User;
import com.bookit.application.types.AccountStatus;
import com.bookit.application.types.Role;
import com.bookit.application.utils.UserUtil;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserService {
    private final CustomUserDetailsService customUserDetailsService;

    public UserService(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    public User createUser(User user) throws IllegalArgumentException, UsernameOrEmailAlreadyExistsException {
        if(!UserUtil.usernameCriteriaFulfilled(user.getUsername())){
            throw new IllegalArgumentException("Entered username does not match required criteria");
        }
        if(!UserUtil.passwordCriteriaFulfilled(user.getPassword())){
            throw new IllegalArgumentException("Entered password does not match required criteria");
        }
        if(UserUtil.emailIsValid(user.getEmail())){
            throw new IllegalArgumentException("Entered e-mail does not match required format");
        }

        List<Role> roles = user.getRoles();
        if(roles.contains(Role.THEATRE_OWNER)){
            user.setAccountStatus(AccountStatus.INACTIVE);
            //TODO: send out an e-mail to notify when the account has been verified and activated
            return this.customUserDetailsService.createUser(user);
        } else if (roles.contains(Role.REGULAR_USER)) {
            user.setAccountStatus(AccountStatus.INACTIVE);
            //TODO: send out an e-mail with account activation link
            return this.customUserDetailsService.createUser(user);
        }
        return null;
    }






}
