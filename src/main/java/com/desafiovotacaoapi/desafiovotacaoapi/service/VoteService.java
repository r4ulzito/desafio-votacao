package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;

import java.util.List;

public interface VoteService {

    Vote createVote(CreateVoteDTO voteDTO);

    List<Vote> getAllByTopicId(Long topicId);

}
