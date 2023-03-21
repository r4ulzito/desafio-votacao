package com.desafiovotacaoapi.desafiovotacaoapi.service.impl;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.TopicMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.service.TopicService;
import com.desafiovotacaoapi.desafiovotacaoapi.service.VoteService;
import com.desafiovotacaoapi.desafiovotacaoapi.util.VotesCounter;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImplements implements TopicService {

    private final TopicRepository topicRepository;
    private final VoteService voteService;

    @Autowired
    public TopicServiceImplements(TopicRepository repository, VoteService voteService) {
        this.topicRepository = repository;
        this.voteService = voteService;
    }

    public Topic createTopic(CreateTopicDTO topicDTO) {

        return this.topicRepository.save(TopicMapper.buildTopic(topicDTO));
    }

    public Topic getTopicById(Long topicId) {

        return this.topicRepository.findById(topicId).get();

    }

    public List<GetTopicDTO> getAllTopics() {
        return this.topicRepository.findAll().stream().map(GetTopicDTO::new).toList();
    }

    public ResultTopicVotesDTO getVotesResult(Long topicId) {

        List<Vote> targetTopicVotes = this.voteService.getAllByTopicId(topicId);

        if (targetTopicVotes.size() == 0) {
            throw new NullQueryResultException("No votes registered!");
        }

        return VotesCounter.countVotes(targetTopicVotes);

    }

}
