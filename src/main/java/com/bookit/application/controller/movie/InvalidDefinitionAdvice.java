package com.bookit.application.controller.movie;


import com.bookit.application.DTO.InvalidDataException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {MovieController.class})
public class InvalidDefinitionAdvice {
    @ExceptionHandler(InvalidDefinitionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String invalidDefinitionHandler(InvalidDefinitionException ex){
        return "Provided data appears to be incorrect. Please check the provided information and try again";
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidInputDataHandler(InvalidDataException e){
        return e.getMessage();
    }
}
