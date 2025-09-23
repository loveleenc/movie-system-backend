package com.bookit.application.controller.ticket;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {TicketsController.class})
public class TicketExceptionsAdvice {

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidTicketStatusHandler(UnsupportedOperationException e){
        return "The ticket status cannot be changed to the requested status.";
    }

}
