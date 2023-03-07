package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;

import java.util.List;

public interface SessionService {

    Session createSession(CreateSessionDTO sessionDTO);

    List<GetSessionDTO> getAllSessions();

    Session getSessionById(Long sessionId);

    Vote newVote(SessionVoteRequestDTO sessionVoteRequestDTO);

}
