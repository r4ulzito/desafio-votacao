package com.desafiovotacaoapi.desafiovotacaoapi.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class DefaultCustomExceptionResponse {

    private Instant timestamp;
    private Integer status;
    private String message;

    public DefaultCustomExceptionResponse(Instant timestamp, Integer status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
