package com.desafiovotacaoapi.desafiovotacaoapi.service.exception.nullQueryResultException;

import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class NullQueryResultExcepetionHandler {

    @ExceptionHandler(NullQueryResultExcepetion.class)
    public ResponseEntity<DefaultCustomExceptionResponse> nullQueryResultExcpetion(NullQueryResultExcepetion ex) {

        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
