package com.desafiovotacaoapi.desafiovotacaoapi.mapper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;

public interface TopicMapper {

    static Topic buildTopic(CreateTopicDTO topicDTO) {
        return Topic.builder()
                .title(topicDTO.title())
                .description(topicDTO.description())
                .build();
    }

}
