package com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.validation.constraints.NotNull;

public record CreateVoteDTO(

        @NotNull(message = "The vote needs a answer!")
        Answer answer,

        @NotNull(message = "The vote needs a associate!")
        Associate associate,

        @NotNull(message = "The vote needs a topic!")
        Topic topic

) {
}
