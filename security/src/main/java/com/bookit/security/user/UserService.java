package com.bookit.security.user;

import com.bookit.security.CustomUserDetailsService;
import com.bookit.security.UsernameOrEmailAlreadyExistsException;
import com.bookit.security.email.EmailService;
import com.bookit.security.entity.User;
import com.bookit.security.entity.types.AccountStatus;
import com.bookit.security.entity.types.Role;
import com.bookit.security.user.token.TokenService;
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
    private UserAccountCreationHelper userAccountCreationHelper;

    public UserService(CustomUserDetailsService customUserDetailsService,
                       TokenService tokenService,
                       EmailService emailService,
                       UserAccountCreationHelper userAccountCreationHelper) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.userAccountCreationHelper = userAccountCreationHelper;
    }

    public User createUser(User user) throws IllegalArgumentException, UsernameOrEmailAlreadyExistsException {
        if (!userAccountCreationHelper.usernameCriteriaFulfilled(user.getUsername())) {
            throw new IllegalArgumentException("Entered username does not match required criteria");
        }
        if (!userAccountCreationHelper.passwordCriteriaFulfilled(user.getPassword())) {
            throw new IllegalArgumentException("Entered password does not match required criteria");
        }
        if (!userAccountCreationHelper.emailIsValid(user.getEmail())) {
            throw new IllegalArgumentException("Entered e-mail does not match required format");
        }

        List<Role> roles = user.getRoles();
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("A new user cannot be created without any role");
        }
        if (roles.contains(Role.ADMIN)) {
            throw new IllegalArgumentException(String.format("Cannot create a user with the role %s", Role.ADMIN.code()));
        }

        user.setAccountStatus(AccountStatus.INACTIVE);
        User createdUser = this.customUserDetailsService.createUser(user);
        if (roles.contains(Role.THEATRE_OWNER)) {
            try {
                this.sendAccountCreationEmail(createdUser);
            } catch (MailSendException | MailParseException | MailAuthenticationException e) {
                throw new AccountActivationException("Unable to send the account activation email", e);
            }
        }
        if (roles.contains(Role.REGULAR_USER)) {
            //TODO: publish a new user event that would be subscribed to by the cart domain
//            this.bookingClient.createCart(createdUser.getId());
            try {
                if (!roles.contains(Role.THEATRE_OWNER)) {
                    this.sendAccountActivationEmail(createdUser);
                }
            } catch (MalformedURLException | MailSendException | MailParseException | MailAuthenticationException e) {
                throw new AccountActivationException("Unable to send the account activation email", e);
            }
        }
        return createdUser;
    }

    public Long getCurrentUserId() {
        return this.customUserDetailsService.getCurrentUserId();
    }

    public void activateUserAccount(String token) throws JwtException, UsernameNotFoundException {
        String username = this.tokenService.getUsernameFromActivationToken(token);
        Boolean accountActivated = this.customUserDetailsService.activateUserAccount(username);
        if (!accountActivated) {
            throw new AccountActivationException("Unable to activate account after fetching user details");
        }
    }

    private void sendAccountCreationEmail(User user) throws MailSendException, MailParseException, MailAuthenticationException {
        String mailMessage = userAccountCreationHelper.createAccountActivationEmailMessage(user.getUsername());
        this.emailService.sendEmail(user.getEmail(), UserAccountCreationHelper.accountActivationEmailSubject, mailMessage);
    }

    private void sendAccountActivationEmail(User user) throws MalformedURLException, MailSendException, MailParseException, MailAuthenticationException {
        UrlResource accountActivationUrl = this.createAccountActivationLink(user);
        String mailMessage = userAccountCreationHelper.createAccountActivationEmailMessage(user.getUsername(), accountActivationUrl);
        this.emailService.sendEmail(user.getEmail(), UserAccountCreationHelper.accountActivationEmailSubject, mailMessage);
    }

    private UrlResource createAccountActivationLink(User user) throws MalformedURLException {
        String token = this.tokenService.createActivationToken(user.getUsername());
        String accountActivationUrl = UriComponentsBuilder.fromUriString(this.clientUrl)
                .pathSegment("#", "user", "activate", token)
                .build().toString();
        return new UrlResource(accountActivationUrl);
    }

    public String getClientUrl() {
        return clientUrl;
    }
}
