package com.bookit.security;

import com.bookit.security.email.EmailProperties;
import com.bookit.security.user.AccountActivationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;
import java.sql.SQLException;


@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionsAdvice {
    @Autowired
    private EmailProperties emailProperties;
    private final Logger logger = LoggerFactory.getLogger(UserExceptionsAdvice.class);

    @ExceptionHandler(UsernameOrEmailAlreadyExistsException.class)
    ResponseEntity<String> usernameOrEmailAlreadyExistsHandler(UsernameOrEmailAlreadyExistsException e){
        return new ResponseEntity<>( "Please select another username or email", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> illegalArgumentHandler(IllegalArgumentException e){
        return new ResponseEntity<>(
                "Data appears to be incorrect in the request. Please check the request details and try again",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountActivationException.class)
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

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<String> dataSourceAccessFailed(DataAccessException e, HttpServletRequest request){
        logger.error("Request {} raised error- {}: {}", request.getRequestURI(), e.getClass(), e.getMessage());
        return new ResponseEntity<>("Unable to respond due to server error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
