package com.desafiovotacaoapi.desafiovotacaoapi.handler;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.SessionClosedException;
import com.desafiovotacaoapi.desafiovotacaoapi.handler.response.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class SessionClosedExceptionHandler {

    @ExceptionHandler(SessionClosedException.class)
    public ResponseEntity<DefaultCustomExceptionResponse> sessionClosedException(SessionClosedException ex) {

        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
