package com.danyatheworst.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String handleInternalServerException() {
        return "internal-error";
    }
}