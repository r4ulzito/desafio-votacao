package com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.validation.constraints.NotNull;

public record CreateVoteDTO(

        @NotNull(message = "The vote needs a answer!")
        Answer answer,

        @NotNull(message = "The vote needs a associate!")
        Long associate_id,

        @NotNull(message = "The vote needs a topic!")
        Long topic_id

) {
}
