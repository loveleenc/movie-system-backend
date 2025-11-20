package com.bookit.application.services.user;

import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.security.UsernameOrEmailAlreadyExistsException;
import com.bookit.application.security.entity.User;
import com.bookit.application.services.email.EmailService;
import com.bookit.application.services.user.token.TokenService;
import com.bookit.application.types.AccountStatus;
import com.bookit.application.types.Role;
import com.bookit.application.utils.UserUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.util.List;

@Component
public class UserService {
    private final CustomUserDetailsService customUserDetailsService;
    @Value("${client.url}")
    private String clientUrl;
    private TokenService tokenService;
    private EmailService emailService;

    public UserService(CustomUserDetailsService customUserDetailsService,
                       TokenService tokenService,
                       EmailService emailService){
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    public User createUser(User user) throws IllegalArgumentException, UsernameOrEmailAlreadyExistsException, MalformedURLException {
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
        if(roles.contains(Role.ADMIN)){
          throw new IllegalArgumentException(String.format("Cannot create a user with the role %s", Role.ADMIN.code()));
        }


        if(roles.contains(Role.THEATRE_OWNER)){
            user.setAccountStatus(AccountStatus.INACTIVE);
            User createdUser =  this.customUserDetailsService.createUser(user);
            try{
                this.sendAccountCreationEmail(createdUser);
                return createdUser;
            }
            catch (MailSendException | MailParseException | MailAuthenticationException e){
                throw new AccountActivationException("Unable to send the account activation email" , e);
            }
        } else if (roles.contains(Role.REGULAR_USER)) {
            user.setAccountStatus(AccountStatus.INACTIVE);
            User createdUser =  this.customUserDetailsService.createUser(user);
            try{
                this.sendAccountActivationEmail(createdUser);
                return createdUser;
            }
            catch (MalformedURLException | MailSendException | MailParseException | MailAuthenticationException e){
                throw new AccountActivationException("Unable to send the account activation email" , e);
            }
        }
        return null;
    }

    public Long getCurrentUserId(){
        return this.customUserDetailsService.getCurrentUserId();
    }

    public void activateUserAccount(String token) throws JwtException, UsernameNotFoundException {
        String username = this.tokenService.getUsernameFromActivationToken(token);
        Boolean accountActivated = this.customUserDetailsService.activateUserAccount(username);
        if(!accountActivated){
            throw new AccountActivationException("Unable to activate account after fetching user details");
        }
    }

    private void sendAccountCreationEmail(User user) throws MailSendException, MailParseException, MailAuthenticationException{
        String mailMessage = UserUtil.createAccountActivationEmailMessage(user.getUsername());
        this.emailService.sendEmail(user.getEmail(), UserUtil.accountActivationEmailSubject, mailMessage);
    }

    private void sendAccountActivationEmail(User user) throws MalformedURLException, MailSendException, MailParseException, MailAuthenticationException {
        UrlResource accountActivationUrl = this.createAccountActivationLink(user);
        String mailMessage = UserUtil.createAccountActivationEmailMessage(user.getUsername(), accountActivationUrl);
        this.emailService.sendEmail(user.getEmail(), UserUtil.accountActivationEmailSubject, mailMessage);
    }

    private UrlResource createAccountActivationLink(User user) throws MalformedURLException {
        String token = this.tokenService.createActivationToken(user.getUsername());
        String accountActivationUrl = UriComponentsBuilder.fromUriString(this.clientUrl)
                .path("account")
                .path("activate")
                .path(token)
                .build().toString();
         return new UrlResource(accountActivationUrl);
    }


}
