package com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import jakarta.validation.constraints.NotNull;

public record CreateVoteDTO(

        @NotNull
        Answer answer,

        @NotNull
        Long associate_id,

        @NotNull
        Long topic_id

) {
}
