package com.desafiovotacaoapi.desafiovotacaoapi.controller.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static com.desafiovotacaoapi.desafiovotacaoapi.SqlProvider.resetDB;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssociateControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Order(1)
    @Test
    public void aaaa() throws Exception {
        mvc.perform(post("/associates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"TESTE\"\n" +
                                "}")
                )
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    @Order(2)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB)
    public void exemploTeste() throws Exception {

        mvc.perform(get("/associates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Order(3)
    @Test
    public void aaaaaaaa() throws Exception {
        mvc.perform(post("/associates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"TESTE2\"\n" +
                                "}")
                )
                .andExpect(jsonPath("$.id").value(1));

    }

//    @Test
////    @SqlGroup({
////            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetDB),
////            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = resetDB)
////    })
//    public void exemploTEste2() {
//
//    }

}
