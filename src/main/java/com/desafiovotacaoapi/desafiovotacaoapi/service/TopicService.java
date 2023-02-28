package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.topicRepository = repository;
    }

    public Topic createTopic(CreateTopicDTO newTopic) {
        return this.topicRepository.save(new Topic(newTopic));
    }

    public List<GetTopicDTO> getAllTopics() {

        return this.topicRepository.findAll().stream().map(GetTopicDTO::new).toList();

    }

}
