package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.AssociateInvalidVoteException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.InvalidTopicException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.SessionClosedException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.VoteMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.SessionRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.service.impl.SessionServiceImplements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    private SessionServiceImplements sessionService;

    @Captor
    private ArgumentCaptor<Session> captor;

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Mock
    private AssociateService associateServiceMock;

    @Mock
    private TopicService topicServiceMock;

    @Mock
    private VoteService voteServiceMock;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.sessionService = new SessionServiceImplements(
                sessionRepositoryMock,
                associateServiceMock,
                topicServiceMock,
                voteServiceMock);
    }

    private List<Session> listSessions() {
        LocalDateTime nowTimeDate = LocalDateTime.now();

        return Arrays.asList(
                new Session(1L, new Topic(1L, "Title1", "Description1"), nowTimeDate, nowTimeDate.plusHours(1), true),
                new Session(2L, new Topic(2L, "Title2", "Description2"), nowTimeDate, nowTimeDate.plusHours(1), true),
                new Session(3L, new Topic(3L, "Title3", "Description3"), nowTimeDate, nowTimeDate.plusHours(1), false)
        );
    }

    @Test
    @DisplayName("Deve criar um session")
    public void createSessionTest() {
        LocalDateTime nowTimeDate = LocalDateTime.now();

        CreateSessionDTO createSessionData = new CreateSessionDTO(nowTimeDate.plusHours(2), 1L);
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");

        Mockito.when(this.topicServiceMock.getTopicById(createSessionData.topic_id())).thenReturn(fakeTopic);

        Mockito.when(sessionRepositoryMock.save(Mockito.any(Session.class)))
                .thenReturn(new Session(1L, fakeTopic, nowTimeDate, nowTimeDate.plusHours(2), true));

        Session createdSession = this.sessionService.createSession(createSessionData);

        Mockito.verify(sessionRepositoryMock).save(captor.capture());
        Session captorSession = captor.getValue();

        assertEquals(createdSession.getDataStart().withNano(0), captorSession.getDataStart().withNano(0));
        assertEquals(createdSession.getDataEnd(), captorSession.getDataEnd());
        assertEquals(createdSession.getTopic().getId(), captorSession.getTopic().getId());
        assertEquals(createdSession.isOpen(), captorSession.isOpen());
        Mockito.verify(sessionRepositoryMock, Mockito.times(1)).save(captorSession);
    }

    @Test
    @DisplayName("Deve lançar uma exceção caso o id do topico seja inválido")
    public void createSessionWithInexistentTopicIdTest() {
        LocalDateTime nowTimeDate = LocalDateTime.now();

        CreateSessionDTO createSessionData = new CreateSessionDTO(nowTimeDate.plusHours(2), 1L);
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");

        Mockito.when(this.topicServiceMock.getTopicById(createSessionData.topic_id()))
                .thenThrow(new NullPointerException("Topic not found!"));

        try {
            Session createdSession = this.sessionService.createSession(createSessionData);
            Mockito.verifyNoInteractions(topicServiceMock);
        } catch (NullPointerException ex) {
            assertEquals(ex.getMessage(), "Topic not found!");
        }

    }

    @Test
    @DisplayName("Deve lançar uma exceção caso ja exista uma sessão referente ao tópico aberta")
    public void createSessionWithUnavailableTopicTest() {
        LocalDateTime nowTimeDate = LocalDateTime.now();

        CreateSessionDTO createSessionData = new CreateSessionDTO(nowTimeDate.plusHours(2), 1L);
        List<Session> topicSessionList = new ArrayList<>();
        topicSessionList.add(Session.builder()
                .id(1L)
                .topic(Topic.builder().id(1L).build())
                .dataEnd(nowTimeDate.plusHours(2)).isOpen(true)
                .build()
        );

        Mockito.when(this.topicServiceMock.getTopicById(createSessionData.topic_id()))
                .thenReturn(Topic.builder().id(1L).build());

        Mockito.when(this.sessionRepositoryMock.findAllByTopicId(1L)).thenReturn(topicSessionList);

        try {
            Session createdSession = this.sessionService.createSession(createSessionData);
            Mockito.verifyNoInteractions(sessionRepositoryMock);
        } catch (InvalidTopicException ex) {
            assertEquals(ex.getMessage(), "Already exist a open session for this topic!");
        }

    }

    @Test
    @DisplayName("Deve retornar uma lista com todas as sessões")
    public void getAllSessionsTest() {

        List<Session> sessionsList = listSessions();

        Mockito.when(this.sessionRepositoryMock.findAll()).thenReturn(sessionsList);

        List<GetSessionDTO> findSessionsList = this.sessionService.getAllSessions();

        assertEquals(findSessionsList.size(), 3);
        assertEquals(findSessionsList.get(0).id(), 1L);
        assertEquals(findSessionsList.get(1).id(), 2L);
        assertEquals(findSessionsList.get(2).id(), 3L);
        Mockito.verify(sessionRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve fechar as sessões em que a data de encerramento ja tenha sido alcançada")
    public void getAllSessionsAndClosingSessionsTest() {
        LocalDateTime nowTimeDate = LocalDateTime.now();

        List<Session> sessionsList = Arrays.asList(
                new Session(1L, new Topic(), nowTimeDate.minusHours(2), nowTimeDate.minusHours(1), true),
                new Session(2L, new Topic(), nowTimeDate, nowTimeDate.plusHours(1), true)
        );

        Mockito.when(this.sessionRepositoryMock.findAll()).thenReturn(sessionsList);

        List<GetSessionDTO> findSessionsList = this.sessionService.getAllSessions();

        assertEquals(findSessionsList.size(), 2);
        assertEquals(findSessionsList.get(0).id(), 1L);
        assertFalse(findSessionsList.get(0).isOpen());
        Mockito.verify(sessionRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia caso nao exista nenhuma sessão registrada")
    public void getAllSessionsWithNoSessionsRegisteredTest() {

        List<Session> sessionsList = new ArrayList<>();

        Mockito.when(this.sessionRepositoryMock.findAll()).thenReturn(sessionsList);

        List<GetSessionDTO> findSessionsList = this.sessionService.getAllSessions();

        assertEquals(findSessionsList.size(), 0);
        Mockito.verify(sessionRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar a sessão referente ao ID passado por parâmetro")
    public void getSessionByIdTest() {

        List<Session> sessionList = listSessions();

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(0)));

        Session findSession = this.sessionService.getSessionById(1L);

        assertEquals(findSession.getId(), 1L);
        assertEquals(findSession.getTopic().getTitle(), "Title1");
        assertEquals(findSession.getTopic().getDescription(), "Description1");
        assertTrue(findSession.isOpen());
        Mockito.verify(sessionRepositoryMock, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar uma exeção caso não exista uma sessão referente ao ID passado por parâmetro")
    public void getSessionByIdWithInexistentSessioIdTest() {

        Mockito.when(sessionRepositoryMock.findById(4L)).thenReturn(Optional.empty());

        try {
            Session findSession = this.sessionService.getSessionById(4L);
            Mockito.verifyNoInteractions(sessionRepositoryMock);
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "Session not found!");
        }

    }

    @Test
    @DisplayName("Deve criar um voto referente ao tópico selecionado")
    public void newVoteTest() {

        Associate fakeAssociate = new Associate(1L, "Associate1");
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");
        List<Session> sessionList = listSessions();

        CreateVoteDTO createSessionVoteDATA = new CreateVoteDTO(Answer.YES, fakeAssociate, fakeTopic);

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(0)));
        Mockito.when(associateServiceMock.getAssociateByID(1L)).thenReturn(fakeAssociate);
        Mockito.when(topicServiceMock.getTopicById(1L)).thenReturn(fakeTopic);
        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(new ArrayList<>());

        Mockito.when(voteServiceMock.createVote(createSessionVoteDATA)).thenReturn(VoteMapper.buildVote(createSessionVoteDATA));

        Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));

        assertEquals(createdSessionVote.getAssociate().getId(), 1L);
        assertEquals(createdSessionVote.getAssociate().getName(), "Associate1");

        assertEquals(createdSessionVote.getTopic().getId(), 1L);
        assertEquals(createdSessionVote.getTopic().getTitle(), "Title1");
        assertEquals(createdSessionVote.getTopic().getDescription(), "Description1");

        assertEquals(createdSessionVote.getAnswer(), Answer.YES);

        Mockito.verify(voteServiceMock, Mockito.times(1)).createVote(createSessionVoteDATA);
    }

    @Test
    @DisplayName("Deve retornar um exceção caso nao exista sessão referente ao ID passado")
    public void newVoteWithInexistentSessionIdTest() {

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
            Mockito.verifyNoInteractions(sessionRepositoryMock);
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "Session not found!");
        }
    }

    @Test
    @DisplayName("Deve lançar uma exceção caso a sessão esteja fechada")
    public void newVoteWithClosedSessionTest() {

        List<Session> sessionList = listSessions();

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(2)));

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
        } catch (SessionClosedException ex) {
            assertEquals(ex.getMessage(), "Session is closed!");
        }

    }

    @Test
    @DisplayName("Deve lançar uma exceção caso a data atual seja maior que a data de encerramento da sessão")
    public void newVoteWithDataEndOutDatedltSessionTest() {
        LocalDateTime nowTimeDate = LocalDateTime.now();
        Session fakeSession = new Session(1L, new Topic(), nowTimeDate, nowTimeDate.minusHours(1), true);

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.of(fakeSession));

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
        } catch (SessionClosedException ex) {
            assertEquals(ex.getMessage(), "Session is closed!");
        }
    }

    @Test
    @DisplayName("Deve lançar uma exceção caso nao exista nenhum associado referente ao ID passado por parâmetro")
    public void newVoteWithInxistentAssociateIdTest() {

        List<Session> sessionList = listSessions();

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(0)));
        Mockito.when(associateServiceMock.getAssociateByID(1L)).thenThrow(new NullQueryResultException("Associate not found!"));

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "Associate not found!");
        }
    }

    @Test
    @DisplayName("Deve lançar uma exceção caso nao exista nenhum topico referente ao ID passado por parâmetro")
    public void newVoteWithInxistentTopicIdTest() {

        Associate fakeAssociate = new Associate(1L, "Associate1");
        List<Session> sessionList = listSessions();

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(0)));
        Mockito.when(associateServiceMock.getAssociateByID(1L)).thenReturn(fakeAssociate);
        Mockito.when(topicServiceMock.getTopicById(1L)).thenThrow(new NullQueryResultException("Topic not found!"));

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
        } catch (NullQueryResultException ex) {
            assertEquals(ex.getMessage(), "Topic not found!");
        }
    }

    @Test
    @DisplayName("Deve lançar uma exceção caso o associado ja tenha votado na pauta referente a sessão")
    public void newVoteWithNoPermissionAssociateVotingTest() {

        Associate fakeAssociate = new Associate(1L, "Associate1");
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");
        List<Session> sessionList = listSessions();

        List<Vote> voteList = new ArrayList<>();
        voteList.add(new Vote(1L, fakeAssociate, fakeTopic, Answer.YES));

        Mockito.when(sessionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(sessionList.get(0)));
        Mockito.when(associateServiceMock.getAssociateByID(1L)).thenReturn(fakeAssociate);
        Mockito.when(topicServiceMock.getTopicById(1L)).thenReturn(fakeTopic);
        Mockito.when(voteServiceMock.getAllByTopicId(1L)).thenReturn(voteList);

        try {
            Vote createdSessionVote = this.sessionService.newVote(new SessionVoteRequestDTO(1L, 1L, Answer.YES));
            Mockito.verifyNoInteractions(voteServiceMock);
        } catch (AssociateInvalidVoteException ex) {
            assertEquals(ex.getMessage(), "Associate already voted for this topic!");
        }

    }
}