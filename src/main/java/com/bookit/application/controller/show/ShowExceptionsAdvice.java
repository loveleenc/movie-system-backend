package com.bookit.application.controller.show;

import com.bookit.application.services.ResourceCreationException;
import com.bookit.application.services.ResourceNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ShowExceptionsAdvice {
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nullDataInRequestHandler(NullPointerException e){
        return "Data appears to be missing in the request. Please check the request details and try again";
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidTicketStatusHandler(UnsupportedOperationException e){
        return "The ticket status cannot be changed to the requested status.";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resourceNotFoundHandler(ResourceNotFoundException e){
        return "Resource not found";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String dataAccessExceptionHandler(DataAccessException e){
        return "Unable to fetch details at the moment. Please try later";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentExceptionHandler(IllegalArgumentException e){
        return "Incorrect data provided in the request. Please check the request details and try again";
    }

    //TODO: add resource creation exception
    @ExceptionHandler(ResourceCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String resourceCreationExceptionHandler(ResourceCreationException e){
        return "Incorrect data provided in the request. Please check the request details and try again";
    }
}
