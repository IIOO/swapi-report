package com.aleksander.test.report.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExternalApiException extends RuntimeException {
    private HttpStatus status;

    public ExternalApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
