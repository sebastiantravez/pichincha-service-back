package com.pichincha.service.exception;

import org.springframework.http.HttpStatus;

public class PichinchaServiceException extends RuntimeException {
    private HttpStatus httpStatus;

    public PichinchaServiceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse getExceptionResponse() {
        return ExceptionResponse.builder()
                .message(getLocalizedMessage())
                .build();
    }
}
