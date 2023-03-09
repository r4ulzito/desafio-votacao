package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.AssociateInvalidVoteException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.InvalidTopicException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.SessionClosedException;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import com.desafiovotacaoapi.desafiovotacaoapi.service.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateSessionDTO> createSessionDTOJson;

    @MockBean
    private SessionService sessionServiceMock;

    @Autowired
    private JacksonTester<SessionVoteRequestDTO> sessionVoteRequestDTOJson;

    @Test
    @DisplayName("Deve retornar status 200 e uma lista com todas as sessões")
    public void getAllSessionsTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);

        List<GetSessionDTO> sessionsList = new ArrayList<>();
        sessionsList.add(new GetSessionDTO(1L, nowLocalDateTime, nowLocalDateTime.plusHours(2), true, new Topic()));
        sessionsList.add(new GetSessionDTO(2L, nowLocalDateTime, nowLocalDateTime.plusHours(2), false, new Topic()));

        Mockito.when(sessionServiceMock.getAllSessions()).thenReturn(sessionsList);

        mvc.perform(get("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].dateStart").value(nowLocalDateTime.toString()))
                .andExpect(jsonPath("$[0].dateEnd").value(nowLocalDateTime.plusHours(2).toString()))
                .andExpect(jsonPath("$[0].isOpen").value("true"));

    }

    @Test
    @DisplayName("Deve retornar status 200 e uma lista vazia caso não exista nenhuma sessão registrada")
    public void getAllSessionsWithNoSessiosRegisteredTest() throws Exception {

        Mockito.when(sessionServiceMock.getAllSessions()).thenReturn(new ArrayList<>());

        mvc.perform(get("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    @DisplayName("Deve retornar status 201 ao criar uma sessão")
    public void createSessionTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(nowLocalDateTime.plusHours(2), 1L);

        Session createdSession = new Session(
                1L,
                new Topic(1L, "Title1", "Description1"),
                nowLocalDateTime,
                createSessionData.data_end(),
                true);

        Mockito.when(sessionServiceMock.createSession(createSessionData))
                .thenReturn(createdSession);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.topic.id").value("1"))
                .andExpect(jsonPath("$.dataStart").value(nowLocalDateTime.toString()))
                .andExpect(jsonPath("$.dataEnd").value(nowLocalDateTime.plusHours(2).toString()))
                .andExpect(jsonPath("$.open").value("true"));

    }

    @Test
    @DisplayName("Deve retornar status 201 e acresentar 1 minuto de duração a sessão caso o tempo de encerramento nao seja enviado")
    public void createSessionWithNullEndDateTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(null, 1L);

        Session createdSession = new Session(
                1L,
                new Topic(1L, "Title1", "Description1"),
                nowLocalDateTime,
                nowLocalDateTime.plusMinutes(1),
                true
        );

        Mockito.when(sessionServiceMock.createSession(createSessionData))
                .thenReturn(createdSession);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.topic.id").value("1"))
                .andExpect(jsonPath("$.dataStart").value(nowLocalDateTime.toString()))
                .andExpect(jsonPath("$.dataEnd").value(nowLocalDateTime.plusMinutes(1).toString()))
                .andExpect(jsonPath("$.open").value("true"));

    }

    @Test
    @DisplayName("Deve retornar status 400 ao passar o ID do tópico como nulo")
    public void createSessionWithNullTopicIDTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(nowLocalDateTime.plusHours(2), null);

        var a = mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The session needs a topic!"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    @Test
    @DisplayName("Deve retornar status 404 caso o topico referente a sessao não exista")
    public void createSessionWithInexistentSessionTopicTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(nowLocalDateTime.plusHours(2), 1L);

        Mockito.when(sessionServiceMock.createSession(createSessionData))
                .thenThrow(new NullQueryResultException("Topic not found!"));

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Topic not found!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

    @Test
    @DisplayName("Deve retornar status 400 caso o tempo de encerramento da sessão enviada seja anterior a data atual")
    public void createSessionWithInvalidSessionEndDateTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(nowLocalDateTime.minusHours(2), 1L);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("End date must be a future date!"))
                .andExpect(jsonPath("$.status").value("400"));

    }

    @Test
    @DisplayName("Deve retornar status 400 caso ja exista uma sessao aberta referente ao topico ")
    public void createSessionWithUnavailableTopicTest() throws Exception {

        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        CreateSessionDTO createSessionData = new CreateSessionDTO(nowLocalDateTime.plusHours(2), 1L);

        Mockito.when(this.sessionServiceMock.createSession(createSessionData))
                .thenThrow(new InvalidTopicException("Already exist a open session for this topic!"));

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Already exist a open session for this topic!"))
                .andExpect(jsonPath("$.status").value("400"));

    }

    @Test
    @DisplayName("Deve retornar status 201 ao votar em um topico")
    public void voteTest() throws Exception {
        LocalDateTime nowLocalDateTime = LocalDateTime.now().withNano(0);
        Topic fakeTopic = new Topic(1L, "Title1", "Description1");
        Session fakeSession = new Session(
                1L,
                fakeTopic,
                nowLocalDateTime,
                nowLocalDateTime.plusMinutes(1),
                true
        );
        Associate fakeAssociate = new Associate(1L, "Associate1");

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Vote createdVote = new Vote(
                1L,
                fakeAssociate,
                fakeTopic,
                Answer.YES
        );

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenReturn(createdVote);

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.associate.id").value("1"))
                .andExpect(jsonPath("$.topic.id").value("1"))
                .andExpect(jsonPath("$.answer").value("YES"));

    }

    @Test
    @DisplayName("Deve retornar status 400 caso o ID da sessao seja nulo")
    public void voteWithNullSessionIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(null, 1L, Answer.YES);

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Need's a session to vote!"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    @Test
    @DisplayName("Deve retornar status 400 caso o ID do associado seja nulo")
    public void voteWithNullAssocaiteIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, null, Answer.YES);

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Need's a associate to vote!"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    @Test
    @DisplayName("Deve retornar status 400 caso o a resposta do voto seja nula")
    public void voteWithNullAnswerTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, null);

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The vote needs a answer!"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    @Test
    @DisplayName("Deve retornar status 404 caso o topico referente a sessao não exista")
    public void voteWithInexistentSessionIDTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenThrow(new NullQueryResultException("Session not found!"));

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

    @Test
    @DisplayName("Deve retornar status 400 caso a sessão referente ao ID enviado esteja fechada")
    public void voteWithClosedSessionIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenThrow(new SessionClosedException("Session is closed!"));

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Session is closed!"))
                .andExpect(jsonPath("$.status").value("400"));

    }

    @Test
    @DisplayName("Deve retornar status 404 caso o associado referente ao ID enviado não exista")
    public void voteWithInexistentAssociateIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenThrow(new NullQueryResultException("Associate not found!"));

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Associate not found!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

    @Test
    @DisplayName("Deve retornar status 404 caso o topico referente a sessão não exista")
    public void voteWithInexistentSessionTopicTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenThrow(new NullQueryResultException("Topic not found!"));

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Topic not found!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

    @Test
    @DisplayName("Deve retornar status 400 o associado ja tenha votado no topico referente a sessao")
    public void voteWithAssociateNoPermissionToVoteTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        Mockito.when(sessionServiceMock.newVote(createVoteRequestData))
                .thenThrow(new AssociateInvalidVoteException("Associate already voted for this topic!"));

        mvc.perform(post("/sessions/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Associate already voted for this topic!"))
                .andExpect(jsonPath("$.status").value("400"));

    }

}