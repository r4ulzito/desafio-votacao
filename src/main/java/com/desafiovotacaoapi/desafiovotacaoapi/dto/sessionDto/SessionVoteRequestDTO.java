package com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record SessionVoteRequestDTO(

        @NotNull(message = "Need's a session to vote!")
        Long session_id,

        @NotNull(message = "Need's a associate to vote!")
        Long associate_id,

        @NotNull(message = "The vote needs a answer!")
        @Enumerated(EnumType.STRING)
        Answer answer
) {
}
