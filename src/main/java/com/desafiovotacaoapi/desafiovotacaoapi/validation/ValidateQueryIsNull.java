package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.nullQueryResultException.NullQueryResultExcpetion;

import java.util.Optional;

public class ValidateQueryIsNull {

    public static void queryIsNull(Optional<?> queryResult, String exceptionMessage) {

        if (queryResult.isEmpty()) {
            throw new NullQueryResultExcpetion(exceptionMessage);
        }

    }

}
