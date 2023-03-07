package com.desafiovotacaoapi.desafiovotacaoapi.handler;

import com.desafiovotacaoapi.desafiovotacaoapi.handler.response.CustomBeanValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomBeanValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomBeanValidationExceptionResponse> BadResquestBeanValidationException(MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        CustomBeanValidationExceptionResponse error = new CustomBeanValidationExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                fieldsMessages,
                fields
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
