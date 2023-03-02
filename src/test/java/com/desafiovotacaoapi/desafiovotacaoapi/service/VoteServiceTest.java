package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest {

    private VoteService service;

    @Captor
    private ArgumentCaptor<Vote> captor;

    @Mock
    private VoteRepository voteRepositoryMock;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.service = new VoteService(voteRepositoryMock);
    }

    private List<Vote> listVotes() {
        List<Vote> list = new ArrayList<>();

        list.add(new Vote(1L, new Associate(), new Topic(1L, "Title1", "Description1"), Answer.YES));
        list.add(new Vote(2L, new Associate(), new Topic(1L, "Title2", "Description2"), Answer.YES));
        list.add(new Vote(3L, new Associate(), new Topic(2L, "Title3", "Description3"), Answer.YES));

        return list;
    }

    @Test
    @DisplayName("Deve criar um novo voto")
    public void createVoteTest() {

        Associate fakeAssociate = new Associate(1L, "Fake Associate");
        Topic fakeTopic = new Topic(1L, "FakeTitle", "FakeDescription");
        CreateVoteDTO createVoteData = new CreateVoteDTO(Answer.YES, fakeAssociate, fakeTopic);

        Mockito.when(voteRepositoryMock.save(Mockito.any(Vote.class))).thenReturn(new Vote(createVoteData));

        Vote createdVote = this.service.createVote(createVoteData);

        Mockito.verify(voteRepositoryMock).save(captor.capture());
        Vote voteCaptor = captor.getValue();

        assertEquals(createdVote.getId(), voteCaptor.getId());
        assertEquals(createdVote.getAnswer(), voteCaptor.getAnswer());
        assertEquals(createdVote.getTopic(), voteCaptor.getTopic());
        Mockito.verify(voteRepositoryMock, Mockito.times(1)).save(voteCaptor);
    }

    @Test
    @DisplayName("Deve retornar todos os votos referentes ao topico enviado por parametro")
    public void getAllByTopicIdTest() {

        List<Vote> listVotes = listVotes();

        Mockito.when(voteRepositoryMock.findAllByTopicId(1L)).thenReturn(listVotes.stream()
                .filter(vote -> vote.getTopic().getId() == 1L).toList());

        List<Vote> topicVotes = this.service.getAllByTopicId(1L);

        assertEquals(topicVotes.size(), 2);
        assertEquals(topicVotes.get(0).getTopic().getTitle(), "Title1");
        assertEquals(topicVotes.get(1).getTopic().getTitle(), "Title2");
        Mockito.verify(voteRepositoryMock).findAllByTopicId(1L);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia caso nao exista nenhum voto referente ao topico")
    public void getAllByTopicIdWithNoResultsTest() {
        List<Vote> listVotes = listVotes();

        Mockito.when(voteRepositoryMock.findAllByTopicId(3L)).thenReturn(listVotes.stream()
                .filter(vote -> vote.getTopic().getId() == 3L).toList());

        List<Vote> topicVotes = this.service.getAllByTopicId(3L);

        assertEquals(topicVotes.size(), 0);
    }

}