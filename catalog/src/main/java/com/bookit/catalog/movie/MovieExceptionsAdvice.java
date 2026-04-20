package com.bookit.catalog.movie;

import com.bookit.catalog.movie.services.MovieException;
import com.bookit.catalog.movie.services.ResourceCreationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice(assignableTypes = MovieController.class)
public class MovieExceptionsAdvice {

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<String> nullDataInRequestHandler(NullPointerException e){
        return new ResponseEntity<>(
        "Data appears to be missing in the request. Please check the request details and try again",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> resourceCreationExceptionHandler(ResourceCreationException e){
        return new ResponseEntity<>("Unable to create or fetch movie details. Please check the request details or try again later."
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void resourceNotFoundHandler(ResourceNotFoundException e){
    }

    @ExceptionHandler(MovieException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String>  movieExceptionHandler(MovieException e){
        return new ResponseEntity<>("Unable to fetch movie(s) at the moment. Please try later",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<String> dataAccessExceptionHandler(DataAccessException e){
        String reason = "Unable to fetch details at the moment. Please try later";
        return new ResponseEntity<>(reason, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> invalidDateFormatHandler(DateTimeParseException e){
        return new ResponseEntity<>("Provided date is not in the acceptable format.",HttpStatus.BAD_REQUEST);
    }

}
