package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@Tag(name = "Topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService service) {
        this.topicService = service;
    }

    @Operation(summary = "Cria um tópico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = {@Content()}),
    })
    @PostMapping
    public ResponseEntity<Topic> createTopic(@Valid @RequestBody CreateTopicDTO data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.topicService.createTopic(data));

    }

    @Operation(summary = "Busca todos os tópicos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<GetTopicDTO>> getAllTopics() {

        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.getAllTopics());

    }

    @Operation(summary = "Busca o resultado dos votos de um tópico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = {@Content()})
    })
    @GetMapping("/result/{id}")
    public ResponseEntity<ResultTopicVotesDTO> getVotesResult(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.getVotesResult(id));

    }

}
