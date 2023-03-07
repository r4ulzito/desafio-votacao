package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;

import java.util.List;

public interface TopicService {

    Topic createTopic(CreateTopicDTO topicDTO);

    Topic getTopicById(Long topicId);

    List<GetTopicDTO> getAllTopics();

    ResultTopicVotesDTO getVotesResult(Long topicId);

}
