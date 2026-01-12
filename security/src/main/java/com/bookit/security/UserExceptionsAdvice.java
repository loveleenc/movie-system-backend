package com.bookit.security;

import com.bookit.security.email.EmailProperties;
import com.bookit.security.user.AccountActivationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;


@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionsAdvice {
    @Autowired
    private EmailProperties emailProperties;

    @ExceptionHandler(UsernameOrEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String usernameOrEmailAlreadyExistsHandler(UsernameOrEmailAlreadyExistsException e){
        return "Please select another username or email";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentHandler(IllegalArgumentException e){
        return "Data appears to be incorrect in the request. Please check the request details and try again";
    }

    @ExceptionHandler(AccountActivationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> accountActivationEmailFailureHandler(AccountActivationException e){
        String message;
        if(e.getCause() instanceof MailSendException ||
                e.getCause() instanceof MalformedURLException ||
                e.getCause() instanceof MailParseException ||
                e.getCause() instanceof MailAuthenticationException){
            message = String.format("An e-mail with the account activation steps was not sent. " +
                    "Please contact us at %s to get your account activated.", emailProperties.getEmailUsername());
        }
        else{
            message = String.format("Unable to activate your account. " +
                    "Please contact us at %s to get your account activated.", emailProperties.getEmailUsername());
        }
        return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
