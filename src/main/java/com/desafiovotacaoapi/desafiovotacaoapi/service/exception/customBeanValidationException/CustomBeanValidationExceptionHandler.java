package com.desafiovotacaoapi.desafiovotacaoapi.service.exception.customBeanValidationException;

import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomBeanValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultCustomExceptionResponse> BadResquestBeanValidationException(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getFieldError();

        assert fieldError != null;
        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                fieldError.getDefaultMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
