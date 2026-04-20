package com.bookit.booking;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {CartController.class})
public class CartExceptionsAdvice {

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<String> dataAccessExceptionHandler(DataAccessException e){
        return new ResponseEntity<>("Unable to fetch details at the moment. Please try later",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TicketBookingException.class)
    ResponseEntity<String> ticketBookingExceptionHandler(TicketBookingException e){
        return new ResponseEntity<>("Data appears to be incorrect in the request. Please check the request details and try again",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<String> resourceNotFoundHandler(ResourceNotFoundException e){
        return new ResponseEntity<>("The requested resource was not found",
                HttpStatus.NOT_FOUND);
    }



}
