package com.bookit.catalog.movie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {MovieControllerInternal.class})
public class MovieExceptionsAdviceInternal {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<String> resourceNotFoundResponseHandler(ResourceNotFoundException e){
        return new ResponseEntity<>("The requested resource was not found", HttpStatus.NOT_FOUND);
    }
}
