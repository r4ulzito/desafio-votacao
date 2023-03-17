package com.desafiovotacaoapi.desafiovotacaoapi.controller.integration;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.insertAssociates;
import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.resetDB;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AssociateControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateAssociateDTO> createAssociateDTOJson;

    private static final String baseURL = "/associates";

    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    @Test
    @DisplayName("Deve retornar status 201 quando associado for criado")
    public void createAssociateTest() throws Exception {

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAssociateDTOJson.write(
                                new CreateAssociateDTO("Associate1")
                        ).getJson())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Associate1"));

    }

    @Test
    @DisplayName("Deve retornar status 400 quando o nome do associado enviado na requisição for nulo")
    public void createAssociateWithNullNameTest() throws Exception {

        mvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAssociateDTOJson.write(
                                new CreateAssociateDTO(null)
                        ).getJson())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The user needs a name!"))
                .andExpect(jsonPath("$.status").value("400"));
    }

    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = insertAssociates),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
    })
    @Test
    @DisplayName("Deve retonar status 200 e uma lista com todos os associados")
    public void getAllAsssociatesTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Associate1"))
                .andExpect(jsonPath("$.[1].name").value("Associate2"));

    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    @Test
    @DisplayName("Deve retonar status 200 e uma lista vazia caso não exista nenhum associado registrado")
    public void getAllAsssociatesWithNoAssociatesRegisteredTest() throws Exception {

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

}
