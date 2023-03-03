package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.service.exception.nullQueryResultException.NullQueryResultExcepetion;
import com.desafiovotacaoapi.desafiovotacaoapi.helper.VotesCounterHelper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.service.validation.ValidateQueryIsNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final VoteService voteService;

    @Autowired
    public TopicService(TopicRepository repository, VoteService voteService) {
        this.topicRepository = repository;
        this.voteService = voteService;
    }

    public Topic createTopic(CreateTopicDTO newTopic) {
        return this.topicRepository.save(new Topic(newTopic));
    }

    public Topic getTopicById(Long topicId) {
        Optional<Topic> topic = this.topicRepository.findById(topicId);

        ValidateQueryIsNull.queryIsNull(topic, "Topic not found!");

        return topic.get();
    }

    public List<GetTopicDTO> getAllTopics() {
        return this.topicRepository.findAll().stream().map(GetTopicDTO::new).toList();
    }

    public ResultTopicVotesDTO getVotesResult(Long topicId) {

        List<Vote> targetTopicVotes = this.voteService.getAllByTopicId(topicId);

        if (targetTopicVotes.size() == 0) {
            throw new NullQueryResultExcepetion("No votes registered!");
        }

        return VotesCounterHelper.countVotes(targetTopicVotes);

    }

}
