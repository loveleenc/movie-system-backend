package com.bookit.application.controller.user;

import com.bookit.application.security.UsernameOrEmailAlreadyExistsException;
import com.bookit.application.services.email.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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

    @ExceptionHandler(AccountActivationEmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> accountActivationEmailFailureHandler(){
        return new ResponseEntity<>(
                String.format("Sending account activation email has failed. " +
                        "Please contact us at %s to get your account activated.", emailProperties.getEmailUsername()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
