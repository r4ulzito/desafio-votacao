package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.CreateAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.associateDto.GetAssociateDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.service.AssociateService;
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
class AssociateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateAssociateDTO> createAssociateDTOJson;

    @MockBean
    private AssociateService associateServiceMock;

    private static final String baseURL = "/associates";

    @Test
    @DisplayName("Deve retornar status 201 quando associado for criado")
    public void createAssociateTest() throws Exception {

        Associate expectAssociate = new Associate(1L, "Associate1");

        Mockito.when(associateServiceMock.createAssociate(Mockito.any(CreateAssociateDTO.class)))
                .thenReturn(expectAssociate);

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

    @Test
    @DisplayName("Deve retonar status 200 e uma lista com todos os associados")
    public void getAllAsssociatesTest() throws Exception {

        List<GetAssociateDTO> associateList = new ArrayList<>();
        associateList.add(new GetAssociateDTO(1L, "Associate1"));
        associateList.add(new GetAssociateDTO(2L, "Associate2"));

        Mockito.when(associateServiceMock.getAllAssociates()).thenReturn(associateList);

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("Associate1"))
                .andExpect(jsonPath("$.[1].name").value("Associate2"));

    }

    @Test
    @DisplayName("Deve retonar status 200 e uma lista vazia caso não exista nenhum associado registrado")
    public void getAllAsssociatesWithNoAssociatesRegisteredTest() throws Exception {

        Mockito.when(associateServiceMock.getAllAssociates()).thenReturn(new ArrayList<>());

        mvc.perform(get(baseURL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

}