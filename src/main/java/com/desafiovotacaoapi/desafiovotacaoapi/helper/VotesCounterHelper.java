package com.desafiovotacaoapi.desafiovotacaoapi.helper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VotesCounterHelper {

    public static ResultTopicVotesDTO countVotes(List<Vote> topicVotes) {

        AtomicInteger yesVotes = new AtomicInteger();
        AtomicInteger noVotes = new AtomicInteger();
        TopicVotesResult result = TopicVotesResult.DRAW;

        topicVotes.forEach(vote -> {
            if (vote.getAnswer().equals(Answer.YES)) {
                yesVotes.addAndGet(1);
            } else {
                noVotes.addAndGet(1);
            }
        });

        if (yesVotes.intValue() > noVotes.intValue()) {
            result = TopicVotesResult.YES;
        } else if (yesVotes.intValue() < noVotes.intValue()) {
            result = TopicVotesResult.NO;
        }

        return new ResultTopicVotesDTO(topicVotes.size(), yesVotes.get(), noVotes.get(), result);
    }

}
