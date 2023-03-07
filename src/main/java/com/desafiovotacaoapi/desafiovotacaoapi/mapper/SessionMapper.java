package com.desafiovotacaoapi.desafiovotacaoapi.mapper;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;

import java.time.LocalDateTime;

public interface SessionMapper {

    static Session buildSession(LocalDateTime dataEnd, Topic targetTopic) {
        return Session.builder()
                .dataStart(LocalDateTime.now())
                .dataEnd(dataEnd)
                .topic(targetTopic)
                .build();
    }

}
