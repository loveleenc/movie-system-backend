package com.bookit.events.shows;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ShowsController.class})
public class ShowExceptionsAdvice {
    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<String> nullDataInRequestHandler(NullPointerException e){
        return new ResponseEntity<>("Data appears to be missing in the request. Please check the request details and try again",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String>  invalidTicketStatusHandler(UnsupportedOperationException e){
        return new ResponseEntity<>("The ticket status cannot be changed to the requested status.",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void resourceNotFoundHandler(ResourceNotFoundException e){
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<String> dataAccessExceptionHandler(DataAccessException e){
        String reason = "Unable to fetch details at the moment. Please try later";
        return new ResponseEntity<>(reason, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException e){
        return new ResponseEntity<>("Incorrect data provided in the request. Please check the request details and try again",HttpStatus.BAD_REQUEST);
    }

    //TODO: add resource creation exception
    @ExceptionHandler(ResourceCreationException.class)
    ResponseEntity<String> resourceCreationExceptionHandler(ResourceCreationException e){
        return new ResponseEntity<>("Incorrect data provided in the request. Please check the request details and try again",HttpStatus.BAD_REQUEST);
    }
}
