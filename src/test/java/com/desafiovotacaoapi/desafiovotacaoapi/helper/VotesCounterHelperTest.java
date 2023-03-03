package com.desafiovotacaoapi.desafiovotacaoapi.helper;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VotesCounterHelperTest {

    @Test
    @DisplayName("Deve retonar a contagem dos votos e o resultado")
    public void countVotesTest() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(2L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(3L, new Associate(), new Topic(), Answer.NO));

        ResultTopicVotesDTO result = VotesCounterHelper.countVotes(listVotes);

        assertEquals(result.totalVotes(), 3);
        assertEquals(result.yesVotes(), 2);
        assertEquals(result.noVotes(), 1);
        assertEquals(result.result(), TopicVotesResult.YES);

    }

}