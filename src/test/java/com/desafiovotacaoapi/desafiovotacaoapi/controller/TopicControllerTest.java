package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.TopicMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.enums.TopicVotesResult;
import com.desafiovotacaoapi.desafiovotacaoapi.service.TopicService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateTopicDTO> createTopicDTOJson;

    @MockBean
    private TopicService topicServiceMock;

    private static final String baseURL = "/topics";

    @Test
    @DisplayName("Deve retornar status 201 ao criar um topico")
    public void createTopicTest() throws Exception {

        CreateTopicDTO createTopicData = new CreateTopicDTO("Title1", "Description1");

        Mockito.when(topicServiceMock.createTopic(createTopicData))
                .thenReturn(TopicMapper.buildTopic(createTopicData));

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
    @DisplayName("Deve retonar status 200 e uma lista com todos os topicos")
    public void getAllTopicsTest() throws Exception {

        List<GetTopicDTO> topicsList = new ArrayList<>();
        topicsList.add(new GetTopicDTO(1L, "Title1", "Description1"));
        topicsList.add(new GetTopicDTO(2L, "Title2", "Description2"));

        Mockito.when(topicServiceMock.getAllTopics()).thenReturn(topicsList);

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

    }

    @Test
    @DisplayName("Deve retonar status 200 e uma lista vazia caso não exista nenhum tópico registrado")
    public void getAllTopicsWithNoTopicsRegisteredTest() throws Exception {

        Mockito.when(topicServiceMock.getAllTopics()).thenReturn(new ArrayList<>());

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    //PAREI AQUI
    @Test
    @DisplayName("Deve retornar status 200 e o resultado da votação do topico")
    public void getVotesResultTest() throws Exception {

        ResultTopicVotesDTO resultVoteData = new ResultTopicVotesDTO(3, 2, 1, TopicVotesResult.YES);

        Mockito.when(topicServiceMock.getVotesResult(1L)).thenReturn(resultVoteData);

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
    @DisplayName("Deve retornar status 404 caso não exista topico referente ao ID passado")
    public void getVotesResultWithInexistentTopicIdTest() throws Exception {

        Mockito.when(topicServiceMock.getVotesResult(2L)).thenThrow(new NullQueryResultException("No votes registered!"));

        mvc.perform(get(baseURL + "/result/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No votes registered!"))
                .andExpect(jsonPath("$.status").value("404"));

    }

}