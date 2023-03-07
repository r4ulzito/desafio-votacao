package com.desafiovotacaoapi.desafiovotacaoapi.service.impl;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.VoteMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.VoteRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImplements implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImplements(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote createVote(CreateVoteDTO voteDTO) {

        return this.voteRepository.save(VoteMapper.buildVote(voteDTO));
    }

    public List<Vote> getAllByTopicId(Long topicId) {
        return this.voteRepository.findAllByTopicId(topicId);
    }

}
