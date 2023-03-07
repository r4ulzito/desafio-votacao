package com.desafiovotacaoapi.desafiovotacaoapi.mapper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;

public interface VoteMapper {

    static Vote buildVote(CreateVoteDTO votoDTO) {
        return Vote.builder()
                .topic(votoDTO.topic())
                .associate(votoDTO.associate())
                .answer(votoDTO.answer())
                .build();
    }

}
