package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ValidateNewSessionEndDateTest {

    @Test
    @DisplayName("Deve retornar o endDate sem alterações")
    public void validateEndDateWithValidEndDateTest() {

        LocalDateTime fakeEndDate = LocalDateTime.now().plusHours(1).withNano(0);

        LocalDateTime validatedEndDate = ValidateNewSessionEndDate.validateEndDate(fakeEndDate);

        assertEquals(validatedEndDate, fakeEndDate);

    }

    @Test
    @DisplayName("Deve retornar o endDate com 1 minuto a mais caso seja nulo")
    public void validateEndDateWithNullEndDateTest() {

        LocalDateTime now = LocalDateTime.now().withNano(0);

        LocalDateTime validatedEndDate = ValidateNewSessionEndDate.validateEndDate(null);

        assertEquals(ChronoUnit.MINUTES.between(now, validatedEndDate.withNano(0)), 1);

    }

}