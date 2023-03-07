package com.desafiovotacaoapi.desafiovotacaoapi.mapper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;

public interface VoteMapper {

    static Vote buildVote(CreateVoteDTO voteDTO) {
        return Vote.builder()
                .topic(voteDTO.topic())
                .associate(voteDTO.associate())
                .answer(voteDTO.answer())
                .build();
    }

}
