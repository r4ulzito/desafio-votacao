package com.desafiovotacaoapi.desafiovotacaoapi.handler;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.InvalidTopicException;
import com.desafiovotacaoapi.desafiovotacaoapi.handler.response.DefaultCustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class InvalidTopicExceptionHandler {

    @ExceptionHandler(InvalidTopicException.class)
    public ResponseEntity<DefaultCustomExceptionResponse> invalidTopicException(InvalidTopicException ex) {

        DefaultCustomExceptionResponse error = new DefaultCustomExceptionResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

}
