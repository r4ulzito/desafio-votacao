package com.desafiovotacaoapi.desafiovotacaoapi.controller.integration;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.resetDB;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(jsonPath("$.status").value(400))
        ;

    }

}
