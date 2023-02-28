package com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSessionDTO(

        @NotNull(message = "The session needs a ends date!")
        LocalDateTime data_end,

        @NotNull(message = "The session needs a topic!")
        Long topic_id

) {

}
