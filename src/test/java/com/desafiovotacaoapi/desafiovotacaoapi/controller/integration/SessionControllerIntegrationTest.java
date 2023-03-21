package com.desafiovotacaoapi.desafiovotacaoapi.controller.integration;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.Answer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateSessionDTO> createSessionDTOJson;

    @Autowired
    private JacksonTester<SessionVoteRequestDTO> sessionVoteRequestDTOJson;

    private static final String baseURL = "/sessions";

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertTopics),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 201 ao criar uma sessão")
    public void createSessionTest() throws Exception {

        CreateSessionDTO createSessionData = new CreateSessionDTO(LocalDateTime.parse("2030-02-19T20:40:09"), 1L);

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.topic.id").value("1"))
                .andExpect(jsonPath("$.dataStart").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.dataEnd").value("2030-02-19T20:40:09"))
                .andExpect(jsonPath("$.open").value("true"));

    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertTopics),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 201 e acresentar 1 minuto de duração a sessão caso o tempo de encerramento nao seja enviado")
    public void createSessionWithNullEndDateTest() throws Exception {

        CreateSessionDTO createSessionData = new CreateSessionDTO(null, 1L);

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createSessionDTOJson.write(
                                createSessionData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.topic.id").value("1"))
                .andExpect(jsonPath("$.dataStart").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.dataEnd").value(LocalDateTime.now().plusMinutes(1).withNano(0).toString()))
                .andExpect(jsonPath("$.open").value("true"));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 400 ao passar o ID do tópico como nulo")
    public void createSessionWithNullTopicIDTest() throws Exception {

        CreateSessionDTO createSessionData = new CreateSessionDTO(LocalDateTime.now().plusHours(2), null);

        var a = mvc.perform(post(baseURL)
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
    @DisplayName("Deve retornar status 400 caso o tempo de encerramento da sessão enviada seja anterior a data atual")
    public void createSessionWithInvalidSessionEndDateTest() throws Exception {

        CreateSessionDTO createSessionData = new CreateSessionDTO(LocalDateTime.now().minusHours(2), 1L);

        mvc.perform(post(baseURL)
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
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessions),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 400 caso ja exista uma sessao aberta referente ao topico ")
    public void createSessionWithUnavailableTopicTest() throws Exception {

        CreateSessionDTO createSessionData = new CreateSessionDTO(LocalDateTime.now().plusHours(2), 1L);

        mvc.perform(post(baseURL)
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
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessions),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 200 e uma lista com todas as sessões")
    public void getAllSessionsTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 200 e uma lista vazia caso não exista nenhuma sessão registrada")
    public void getAllSessionsWithNoSessiosRegisteredTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessions),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertAssociates),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 201 ao votar em um topico")
    public void voteTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        mvc.perform(post(baseURL + "/vote")
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
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 400 caso o ID da sessao seja nulo")
    public void voteWithNullSessionIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(null, 1L, Answer.YES);

        mvc.perform(post(baseURL + "/vote")
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
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 400 caso o ID do associado seja nulo")
    public void voteWithNullAssocaiteIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, null, Answer.YES);

        mvc.perform(post(baseURL + "/vote")
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
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 400 caso o a resposta do voto seja nula")
    public void voteWithNullAnswerTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, null);

        mvc.perform(post(baseURL + "/vote")
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
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessions),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 404 caso o associado referente ao ID enviado não exista")
    public void voteWithInexistentAssociateIdTest() throws Exception {

        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);

        mvc.perform(post(baseURL + "/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionVoteRequestDTOJson.write(
                                createVoteRequestData
                        ).getJson())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Associate not found!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

//    @Test
//    @SqlGroup({
//            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertSessions),
//            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
//    })
//    @DisplayName("Deve retornar status 400 o associado ja tenha votado no topico referente a sessao")
//    public void voteWithAssociateNoPermissionToVoteTest() throws Exception {
//
//        SessionVoteRequestDTO createVoteRequestData = new SessionVoteRequestDTO(1L, 1L, Answer.YES);
//
//        mvc.perform(post(baseURL + "/vote")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(sessionVoteRequestDTOJson.write(
//                                createVoteRequestData
//                        ).getJson())
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Associate already voted for this topic!"))
//                .andExpect(jsonPath("$.status").value("400"));
//
//    }

}
