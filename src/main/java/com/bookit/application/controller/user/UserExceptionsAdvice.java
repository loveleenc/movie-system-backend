package com.bookit.application.controller.user;

import com.bookit.application.security.UsernameOrEmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionsAdvice {

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
}
