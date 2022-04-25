package com.pichincha.service.exception.handler;

import com.pichincha.service.exception.ExceptionResponse;
import com.pichincha.service.exception.PichinchaServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> genericExceptionHandler(Exception exception) {
        HttpStatus internalServerError;
        if (exception instanceof AccessDeniedException) {
            internalServerError = HttpStatus.FORBIDDEN;
        } else {
            internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message("Ocurrió un error inesperado")
                .build();

        ResponseEntity<ExceptionResponse> response = new ResponseEntity<>(
                exceptionResponse,
                internalServerError
        );
        return response;
    }

    @ExceptionHandler(PichinchaServiceException.class)
    public ResponseEntity<ExceptionResponse> customExceptionHandler(PichinchaServiceException exception) {
        ResponseEntity<ExceptionResponse> response = new ResponseEntity<>(exception.getExceptionResponse(), exception.getHttpStatus());
        return response;
    }

}
