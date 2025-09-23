package com.bookit.application.controller;


import com.bookit.application.controller.movie.MovieController;
import com.bookit.application.controller.ticket.TicketsController;
import com.bookit.application.dto.InvalidDataException;
import com.bookit.application.services.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {MovieController.class, TicketsController.class})
public class InvalidDefinitionAdvice {
    @ExceptionHandler(InvalidDefinitionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String invalidDefinitionHandler(InvalidDefinitionException ex){
        return "Provided data appears to be incorrect. Please check the provided information and try again";
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidInputDataHandler(InvalidDataException e){
        return "Invalid input data provided. Please correct the provided inputs and try again. ";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String resourceNotFoundHandler(ResourceNotFoundException e){
        return "The requested resource is not found";
    }
}
