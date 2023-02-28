package com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;

public record ResultTopicVotesDTO(

        Integer totalVotes,
        Integer yesVotes,
        Integer noVotes,
        TopicVotesResult result

) {
}
