package com.bookit.catalog.movie;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {MovieControllerInternal.class})
public class MovieExceptionsAdviceInternal {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resourceNotFoundResponseHandler(ResourceNotFoundException e){
        return "The requested resource was not found";
    }
}
