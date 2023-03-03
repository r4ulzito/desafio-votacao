package com.desafiovotacaoapi.desafiovotacaoapi.service.validation;

import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.invalidDateEndException.InvalidDateEndException;

import java.time.LocalDateTime;

public class ValidateNewSessionEndDate {

    public static LocalDateTime validateEndDate(LocalDateTime endDate) {

        LocalDateTime today = LocalDateTime.now();

        if (endDate == null) {
            return today.plusMinutes(1);
        } else if (endDate.isBefore(today)) {
            throw new InvalidDateEndException("Invalid session ends date!");
        }

        return endDate;

    }

}
