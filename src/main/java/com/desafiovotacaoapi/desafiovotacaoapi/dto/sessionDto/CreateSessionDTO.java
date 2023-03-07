package com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSessionDTO(

        @Future(message = "End date must be a future date!")
        LocalDateTime data_end,

        @NotNull(message = "The session needs a topic!")
        Long topic_id

) {

}
