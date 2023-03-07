package com.desafiovotacaoapi.desafiovotacaoapi.validation;

import java.time.LocalDateTime;

public class ValidateNewSessionEndDate {

    public static LocalDateTime validateEndDate(LocalDateTime endDate) {

        LocalDateTime today = LocalDateTime.now();

        if (endDate == null) {
            return today.plusMinutes(1);
        }

        return endDate;

    }

}
