package com.desafiovotacaoapi.desafiovotacaoapi.controller.integration;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TopicControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateTopicDTO> createTopicDTOJson;

    private static final String baseURL = "/topics";

    @Test
    @DisplayName("Deve retornar status 201 ao criar um topico")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    public void createTopicTest() throws Exception {

        CreateTopicDTO createTopicData = new CreateTopicDTO("Title1", "Description1");

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTopicDTOJson.write(
                                createTopicData
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Title1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    @DisplayName("Deve retornar status 400 ao criar um topico com titulo nulo")
    public void createTopicWithNullTitleTest() throws Exception {

        CreateTopicDTO createTopicData = new CreateTopicDTO(null, "Description1");

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTopicDTOJson.write(
                                createTopicData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The topic needs a title!"))
                .andExpect(jsonPath("$.status").value(400));

    }

    @Test
    @DisplayName("Deve retornar status 400 ao criar um topico com descriçao nula")
    public void createTopicWithNullDescriptionTest() throws Exception {

        CreateTopicDTO createTopicData = new CreateTopicDTO("Title1", null);

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTopicDTOJson.write(
                                createTopicData
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The topic needs a description!"))
                .andExpect(jsonPath("$.status").value(400))
        ;

    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertTopics),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retonar status 200 e uma lista com todos os topicos")
    public void getAllTopicsTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retonar status 200 e uma lista vazia caso não exista nenhum tópico registrado")
    public void getAllTopicsWithNoTopicsRegisteredTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertVotes),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @DisplayName("Deve retornar status 200 e o resultado da votação do topico")
    public void getVotesResultTest() throws Exception {

        mvc.perform(get(baseURL + "/result/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVotes").value("3"))
                .andExpect(jsonPath("$.yesVotes").value("2"))
                .andExpect(jsonPath("$.noVotes").value("1"))
                .andExpect(jsonPath("$.result").value("YES"));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @DisplayName("Deve retornar status 404 caso não exista topico referente ao ID passado")
    public void getVotesResultWithInexistentTopicIdTest() throws Exception {

        mvc.perform(get(baseURL + "/result/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No votes registered!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

    @Test
    @DisplayName("Deve retornar status 404 caso o ID passado seja nulo")
    public void getVotesResultWithNullTopicIdTest() throws Exception {

        mvc.perform(get(baseURL + "/results/")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

    }
}
