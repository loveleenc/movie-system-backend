package com.bookit.application.controller.ticket;


import com.bookit.application.common.ResourceNotFoundException;
import com.bookit.application.services.TicketBookingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {TicketsController.class})
public class TicketExceptionsAdvice {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nullDataInRequestHandler(NullPointerException e){
        return "Data appears to be missing or incorrect in the request. Please check the request details and try again";
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

    @ExceptionHandler(TicketBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String ticketBookingExceptionHandler(TicketBookingException e){
        return "The ticket(s) cannot be booked/cancelled";
    }

}
