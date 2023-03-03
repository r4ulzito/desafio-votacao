package com.desafiovotacaoapi.desafiovotacaoapi.service.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.nullQueryResultException.NullQueryResultExcepetion;
import com.desafiovotacaoapi.desafiovotacaoapi.service.validation.ValidateQueryIsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValidateQueryIsNullTest {

    @Test
    @DisplayName("Deve lançar uma exeção caso o resultado da pesquisa validado seja nulo")
    public void queryIsNullWithNullQueryResultTest() {

        Optional queryResultFake = Optional.empty();

        try {
            ValidateQueryIsNull.queryIsNull(queryResultFake, "Query is null!");
        } catch (NullQueryResultExcepetion ex) {
            assertEquals(ex.getMessage(), "Query is null!");
        }

    }

    @Test
    @DisplayName("Não deve fazer nada caso o resultado da pesquisa seja diferente de nulo")
    public void queryIsNullWithNotNullQueryResultTest() {

        Optional<Integer> queryResultFake = Optional.of(1);

        try {
            ValidateQueryIsNull.queryIsNull(queryResultFake, "Query is null!");
        } catch (NullQueryResultExcepetion ex) {
            assertNull(ex);
        }

    }

}