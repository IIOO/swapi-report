package com.aleksander.test.report.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ExceptionDto> noReportWithGivenIdException() {
        return new ResponseEntity<>(new ExceptionDto("No report with given id"), HttpStatus.NOT_FOUND);
    }
}
