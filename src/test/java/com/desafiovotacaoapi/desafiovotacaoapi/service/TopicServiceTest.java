package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.TopicMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.service.impl.TopicServiceImplements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TopicServiceTest {

    private TopicServiceImplements service;

    @Captor
    private ArgumentCaptor<Topic> captor;

    @Mock
    private TopicRepository topicRepositoryMock;

    @Mock
    private VoteService voteServiceMock;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.service = new TopicServiceImplements(topicRepositoryMock, voteServiceMock);
    }

    private List<Topic> topicsList() {

        List<Topic> list = new ArrayList<>();

        list.add(new Topic(1L, "Title1", "Description1"));
        list.add(new Topic(2L, "Title2", "Description2"));
        list.add(new Topic(3L, "Title3", "Description3"));

        return list;
    }

    @Test
    @DisplayName("Deve criar um topico")
    public void createTopicTest() {

        CreateTopicDTO createTopicData = new CreateTopicDTO("Title1", "Description1");

        Mockito.when(topicRepositoryMock.save(Mockito.any(Topic.class)))
                .thenReturn(TopicMapper.buildTopic(createTopicData));

        Topic createdTopic = this.service.createTopic(createTopicData);

        Mockito.verify(topicRepositoryMock).save(captor.capture());
        Topic topicCaptor = captor.getValue();

        assertEquals(createdTopic.getTitle(), topicCaptor.getTitle());
        assertEquals(createdTopic.getDescription(), topicCaptor.getDescription());
        Mockito.verify(topicRepositoryMock, Mockito.times(1)).save(topicCaptor);
    }

    @Test
    @DisplayName("Deve retornar o topico referente ao ID passa dor parâmetro")
    public void getTopicByIdTest() {

        List<Topic> topicsList = topicsList();

        Mockito.when(topicRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(topicsList.get(0)));

        Topic findTopic = this.service.getTopicById(1L);

        assertEquals(findTopic.getId(), 1L);
        assertEquals(findTopic.getTitle(), "Title1");
        assertEquals(findTopic.getDescription(), "Description1");
        Mockito.verify(topicRepositoryMock, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar uma Exceção caso nao encontre nenhum topico com o ID indicado")
    public void getTopicByIdWithInexistentTopicIdTest() {

        Mockito.when(topicRepositoryMock.findById(4L)).thenReturn(Optional.empty());

        try {
            Topic findTopic = this.service.getTopicById(4L);
            Mockito.verifyNoInteractions(topicRepositoryMock);
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "Topic not found!");
        }

    }

    @Test
    @DisplayName("Deve retornar todos os topicos")
    public void getAllTopicsTest() {

        List<Topic> topicList = topicsList();

        Mockito.when(topicRepositoryMock.findAll()).thenReturn(topicList);

        List<GetTopicDTO> allTopicsList = this.service.getAllTopics();

        assertEquals(allTopicsList.size(), 3);
        assertEquals(allTopicsList.get(0).id(), 1L);
        assertEquals(allTopicsList.get(1).id(), 2L);
        assertEquals(allTopicsList.get(2).id(), 3L);
        Mockito.verify(topicRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve uma lista vazia caso nao exista nenhum topico registrado")
    public void getAllTopicsWithNoneTopicTest() {

        List<Topic> topicList = new ArrayList<>();

        Mockito.when(topicRepositoryMock.findAll()).thenReturn(topicList);

        List<GetTopicDTO> allTopicsList = this.service.getAllTopics();

        assertEquals(allTopicsList.size(), 0);
        Mockito.verify(topicRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um exceção caso não exista nenhum voto registrado no topico")
    public void getVotesResultWithNoVotesRegistered() {

        List<Vote> listVotes = new ArrayList<>();

        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(listVotes);

        try {
            ResultTopicVotesDTO topicVotesResult = this.service.getVotesResult(1L);
            Mockito.verifyNoInteractions(voteServiceMock);
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "No votes registered!");
        }

    }

    @Test
    @DisplayName("Deve retornar o resultado da votação como YES")
    public void getVotesResultWithYesResult() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(1L, "Title1", "Description1"), Answer.YES));
        listVotes.add(new Vote(2L, new Associate(), new Topic(1L, "Title2", "Description2"), Answer.YES));

        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(listVotes.stream()
                .filter(vote -> vote.getTopic().getId() == 1L).toList());

        ResultTopicVotesDTO topicVotesResult = this.service.getVotesResult(1L);

        assertEquals(topicVotesResult.totalVotes(), 2);
        assertEquals(topicVotesResult.yesVotes(), 2);
        assertEquals(topicVotesResult.noVotes(), 0);
        assertEquals(topicVotesResult.result(), TopicVotesResult.YES);
        Mockito.verify(voteServiceMock).getAllByTopicId(1L);
    }

    @Test
    @DisplayName("Deve retornar o resultado da votação como NO")
    public void getVotesResultWithNoResult() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(1L, "Title1", "Description1"), Answer.NO));
        listVotes.add(new Vote(2L, new Associate(), new Topic(1L, "Title2", "Description2"), Answer.NO));

        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(listVotes.stream()
                .filter(vote -> vote.getTopic().getId() == 1L).toList());

        ResultTopicVotesDTO topicVotesResult = this.service.getVotesResult(1L);

        assertEquals(topicVotesResult.totalVotes(), 2);
        assertEquals(topicVotesResult.yesVotes(), 0);
        assertEquals(topicVotesResult.noVotes(), 2);
        assertEquals(topicVotesResult.result(), TopicVotesResult.NO);
        Mockito.verify(voteServiceMock).getAllByTopicId(1L);
    }

    @Test
    @DisplayName("Deve retornar o resultado da votação como DRAW")
    public void getVotesResultWithDRAWResult() {

        List<Vote> listVotes = new ArrayList<>();

        listVotes.add(new Vote(1L, new Associate(), new Topic(1L, "Title1", "Description1"), Answer.NO));
        listVotes.add(new Vote(2L, new Associate(), new Topic(1L, "Title2", "Description2"), Answer.YES));

        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(listVotes.stream()
                .filter(vote -> vote.getTopic().getId() == 1L).toList());

        ResultTopicVotesDTO topicVotesResult = this.service.getVotesResult(1L);

        assertEquals(topicVotesResult.totalVotes(), 2);
        assertEquals(topicVotesResult.yesVotes(), 1);
        assertEquals(topicVotesResult.noVotes(), 1);
        assertEquals(topicVotesResult.result(), TopicVotesResult.DRAW);
        Mockito.verify(voteServiceMock).getAllByTopicId(1L);
    }
}