package com.desafiovotacaoapi.desafiovotacaoapi.util;

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

class VotesCounterTest {

    @Test
    @DisplayName("Deve retonar a contagem dos votos e o result YES")
    public void countVotesWithYesResultTest() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(2L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(3L, new Associate(), new Topic(), Answer.NO));

        ResultTopicVotesDTO result = VotesCounter.countVotes(listVotes);

        assertEquals(result.totalVotes(), 3);
        assertEquals(result.yesVotes(), 2);
        assertEquals(result.noVotes(), 1);
        assertEquals(result.result(), TopicVotesResult.YES);

    }

    @Test
    @DisplayName("Deve retonar a contagem dos votos e o result NO")
    public void countVotesWithNoResultTest() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(2L, new Associate(), new Topic(), Answer.NO));
        listVotes.add(new Vote(3L, new Associate(), new Topic(), Answer.NO));

        ResultTopicVotesDTO result = VotesCounter.countVotes(listVotes);

        assertEquals(result.totalVotes(), 3);
        assertEquals(result.yesVotes(), 1);
        assertEquals(result.noVotes(), 2);
        assertEquals(result.result(), TopicVotesResult.NO);

    }

    @Test
    @DisplayName("Deve retonar a contagem dos votos e o result DRAW")
    public void countVotesWithDrawResultTest() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(), Answer.YES));
        listVotes.add(new Vote(2L, new Associate(), new Topic(), Answer.NO));

        ResultTopicVotesDTO result = VotesCounter.countVotes(listVotes);

        assertEquals(result.totalVotes(), 2);
        assertEquals(result.yesVotes(), 1);
        assertEquals(result.noVotes(), 1);
        assertEquals(result.result(), TopicVotesResult.DRAW);

    }

}