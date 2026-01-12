package com.bookit.catalog.movie;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice(assignableTypes = MovieController.class)
public class MovieExceptionsAdvice {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nullDataInRequestHandler(NullPointerException e){
        return "Data appears to be missing in the request. Please check the request details and try again";
    }

    @ExceptionHandler(ResourceCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String resourceCreationExceptionHandler(ResourceCreationException e){
        return "Unable to create or fetch movie details. Please check the request details or try again later.";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resourceNotFoundHandler(ResourceNotFoundException e){
        return "The requested resource was not found";
    }

    @ExceptionHandler(MovieException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String movieExceptionHandler(MovieException e){
        return "Unable to fetch movie(s) at the moment. Please try later";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String dataAccessExceptionHandler(DataAccessException e){
        return "Unable to fetch details at the moment. Please try later";
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidDateFormatHandler(DateTimeParseException e){
        return "Provided date is not in the acceptable format.";
    }

}
