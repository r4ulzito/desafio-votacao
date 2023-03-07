package com.desafiovotacaoapi.desafiovotacaoapi.handler.response;

import java.time.Instant;

public class CustomBeanValidationExceptionResponse extends DefaultCustomExceptionResponse {

    private final String fields;

    public CustomBeanValidationExceptionResponse(Instant timestamp, Integer status, String message, String fields) {
        super(timestamp, status, message);
        this.fields = fields;
    }

    public String getFields() {
        return this.fields;
    }
}
