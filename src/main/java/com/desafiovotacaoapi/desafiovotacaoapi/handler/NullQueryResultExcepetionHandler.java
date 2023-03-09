package com.desafiovotacaoapi.desafiovotacaoapi.handler;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.handler.response.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class NullQueryResultExcepetionHandler {

    @ExceptionHandler(NullQueryResultException.class)
    public ResponseEntity<DefaultCustomExceptionResponse> nullQueryResultExcpetion(NullQueryResultException ex) {

        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

}
