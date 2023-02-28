package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.SessionRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.TopicRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateNewSessionDateEnd;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateQueryIsNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, TopicRepository topicRepository) {
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
    }

    public Session createSession(CreateSessionDTO newSession) {

        Optional<Topic> targetTopic = topicRepository.findById(newSession.topic_id());

        ValidateQueryIsNull.queryIsNull(targetTopic, "Topic not found!");

        ValidateNewSessionDateEnd.validDateEnd(newSession.data_end());

        return this.sessionRepository.save(new Session(newSession.data_end(), targetTopic.get()));

    }

}
