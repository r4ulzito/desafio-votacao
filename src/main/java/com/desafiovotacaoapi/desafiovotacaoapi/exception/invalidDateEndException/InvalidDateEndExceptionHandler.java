package com.desafiovotacaoapi.desafiovotacaoapi.exception.invalidDateEndException;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class InvalidDateEndExceptionHandler {

    @ExceptionHandler(InvalidDateEndException.class)
    public ResponseEntity<DefaultCustomExceptionResponse> InvalidDateException(InvalidDateEndException ex) {

        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
