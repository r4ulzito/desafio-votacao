package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.service.SessionService;
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
@RequestMapping("/sessions")
@Tag(name = "Session")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Busca todas as sessões")
    @ApiResponses(value = {@ApiResponse(responseCode = "200")})
    @GetMapping
    public ResponseEntity<List<GetSessionDTO>> getAllSessions() {

        return ResponseEntity.status(HttpStatus.OK).body(this.sessionService.getAllSessions());

    }

    @Operation(summary = "Cria uma sessão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = {@Content()}),
            @ApiResponse(responseCode = "404", content = {@Content()})
    })
    @PostMapping
    public ResponseEntity<Session> createSession(@Valid @RequestBody CreateSessionDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.sessionService.createSession(data));
    }

    @Operation(summary = "Cria um voto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = {@Content()}),
            @ApiResponse(responseCode = "404", content = {@Content()})
    })
    @PostMapping("/vote")
    public ResponseEntity<Vote> vote(@Valid @RequestBody SessionVoteRequestDTO data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.sessionService.newVote(data));

    }

}
