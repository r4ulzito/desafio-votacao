package com.desafiovotacaoapi.desafiovotacaoapi.service.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.nullQueryResultException.NullQueryResultExcepetion;

import java.util.Optional;

public class ValidateQueryIsNull {

    public static void queryIsNull(Optional<?> queryResult, String exceptionMessage) {

        if (queryResult.isEmpty()) {
            throw new NullQueryResultExcepetion(exceptionMessage);
        }

    }

}
