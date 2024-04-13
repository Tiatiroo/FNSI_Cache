package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.dto.ErrorObject;
import com.fnsi.fnsi_cache.exception.FNSIParsingException;
import com.fnsi.fnsi_cache.exception.FNSIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerException {
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleException(Exception e) {

        ErrorObject errorObject = new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleFNSIParsingException(FNSIParsingException e) {

        ErrorObject errorObject = new ErrorObject(HttpStatus.UNPROCESSABLE_ENTITY.value(),e.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(errorObject,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleFNSIException(FNSIException e) {

        ErrorObject errorObject = new ErrorObject(HttpStatus.NOT_FOUND.value(),e.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(errorObject,HttpStatus.NOT_FOUND);
    }

}
