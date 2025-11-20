package com.bookit.application.services.user;

import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.security.UsernameOrEmailAlreadyExistsException;
import com.bookit.application.security.entity.User;
import com.bookit.application.services.CartService;
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
import org.springframework.web.util.UriBuilder;
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
    private CartService cartService;

    public UserService(CustomUserDetailsService customUserDetailsService,
                       TokenService tokenService,
                       EmailService emailService,
                       CartService cartService){
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.cartService = cartService;
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

        user.setAccountStatus(AccountStatus.INACTIVE);
        User createdUser =  this.customUserDetailsService.createUser(user);
        if(roles.contains(Role.THEATRE_OWNER)){
            try{
                this.sendAccountCreationEmail(createdUser);
            }
            catch (MailSendException | MailParseException | MailAuthenticationException e){
                throw new AccountActivationException("Unable to send the account activation email" , e);
            }
        } if (roles.contains(Role.REGULAR_USER)) {
            this.cartService.createCartForNewUser(createdUser.getId());
            try{
                if(!roles.contains(Role.THEATRE_OWNER)){
                    this.sendAccountActivationEmail(createdUser);
                }
            }
            catch (MalformedURLException | MailSendException | MailParseException | MailAuthenticationException e){
                throw new AccountActivationException("Unable to send the account activation email" , e);
            }
        }
        return createdUser;
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
                .pathSegment("user", "activate", token)
                .build().toString();
         return new UrlResource(accountActivationUrl);
    }


}
