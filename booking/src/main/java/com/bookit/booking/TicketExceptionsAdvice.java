package com.bookit.booking;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {TicketsController.class})
public class TicketExceptionsAdvice {

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<String> nullDataInRequestHandler(NullPointerException e){
        return new ResponseEntity<>("Data appears to be missing or incorrect in the request. Please check the request details and try again",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    ResponseEntity<String> invalidTicketStatusHandler(UnsupportedOperationException e){
         return new ResponseEntity<>("The ticket status cannot be changed to the requested status.",
                HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(TicketBookingException.class)
    ResponseEntity<String> ticketBookingExceptionHandler(TicketBookingException e){
        String reason =  "The ticket(s) cannot be booked/cancelled";
        return new ResponseEntity<>(reason, HttpStatus.BAD_REQUEST);
    }

}
