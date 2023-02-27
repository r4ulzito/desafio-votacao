package com.desafiovotacaoapi.desafiovotacaoapi.exception.customBeanValidationException;

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
    public ResponseEntity<BeanValidationCustomResponse> BadResquestException(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getFieldError();

        BeanValidationCustomResponse error = new BeanValidationCustomResponse();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        assert fieldError != null;
        error.setMessage(fieldError.getDefaultMessage());
        error.setFieldName(fieldError.getField());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
