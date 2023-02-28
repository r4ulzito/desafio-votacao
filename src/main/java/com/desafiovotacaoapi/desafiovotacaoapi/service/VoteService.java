package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.nullQueryResultException.NullQueryResultExcpetion;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.AssociateRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final AssociateRepository associateRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, AssociateRepository associateRepository, TopicRepository topicRepository) {
        this.voteRepository = voteRepository;
        this.associateRepository = associateRepository;
        this.topicRepository = topicRepository;
    }

    public Vote createVote(CreateVoteDTO newVote) {

        Optional<Associate> targetAssociate = this.associateRepository.findById(newVote.associate_id());
        Optional<Topic> targetTopic = this.topicRepository.findById(newVote.topic_id());

        if (targetAssociate.isEmpty()) {
            throw new NullQueryResultExcpetion("Associate not found!");
        } else if (targetTopic.isEmpty()) {
            throw new NullQueryResultExcpetion("Topic not found!");
        } else {
            return this.voteRepository.save(new Vote(newVote.answer(), targetAssociate.get(), targetTopic.get()));
        }

    }

}
