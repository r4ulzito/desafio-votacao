package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.exception.invalidDateEndException.InvalidDateEndException;

import java.time.LocalDateTime;

public class ValidateNewSessionDateEnd {

    public static void validDateEnd(LocalDateTime endDate) {

        LocalDateTime today = LocalDateTime.now();

        if (endDate.isBefore(today)) {
            throw new InvalidDateEndException("Invalid session ends date!");
        }

    }

}
