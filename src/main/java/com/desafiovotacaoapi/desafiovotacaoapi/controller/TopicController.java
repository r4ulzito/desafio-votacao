package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.CreateTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.GetTopicDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.topicDto.ResultTopicVotesDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService service) {
        this.topicService = service;
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@Valid @RequestBody CreateTopicDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.topicService.createTopic(data));
    }

    @GetMapping
    public ResponseEntity<List<GetTopicDTO>> getAllTopics() {

        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.getAllTopics());

    }

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultTopicVotesDTO> getVotesResult(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(this.topicService.getVotesResult(id));

    }

}
