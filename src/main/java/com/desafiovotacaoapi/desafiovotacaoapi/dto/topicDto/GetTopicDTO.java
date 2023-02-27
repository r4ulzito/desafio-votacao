package com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;

public record GetTopicDTO(
        Long id,
        String title,
        String description
) {

    public GetTopicDTO(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getDescription());
    }
}
