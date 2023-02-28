package com.desafiovotacaoapi.desafiovotacaoapi.controller;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @PostMapping("/vote")
    public ResponseEntity<Vote> vote(@Valid @RequestBody CreateVoteDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Vote());
    }

}
