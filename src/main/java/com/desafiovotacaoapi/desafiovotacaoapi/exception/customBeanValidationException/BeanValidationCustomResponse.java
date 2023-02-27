package com.desafiovotacaoapi.desafiovotacaoapi.exception.customBeanValidationException;

import lombok.Data;

import java.time.Instant;

@Data
public class BeanValidationCustomResponse {

    private Instant timestamp;
    private Integer status;
    private String message;
    private String fieldName;

}
