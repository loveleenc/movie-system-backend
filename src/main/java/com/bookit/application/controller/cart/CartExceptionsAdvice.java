package com.bookit.application.controller.cart;

import com.bookit.application.common.ResourceNotFoundException;
import com.bookit.application.services.TicketBookingException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {CartController.class})
public class CartExceptionsAdvice {

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String dataAccessExceptionHandler(DataAccessException e){
        return "Unable to fetch details at the moment. Please try later";
    }

    @ExceptionHandler(TicketBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String ticketBookingExceptionHandler(TicketBookingException e){
        return "Data appears to be incorrect in the request. Please check the request details and try again";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resourceNotFoundHandler(ResourceNotFoundException e){
        return "The requested resource was not found";
    }



}
