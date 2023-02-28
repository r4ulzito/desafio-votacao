package com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;

import java.time.LocalDateTime;

public record GetSessionDTO(
        Long id,
        LocalDateTime dateStart,
        LocalDateTime dateEnd,
        boolean isOpen,
        Topic topic
) {

    public GetSessionDTO(Session session) {
        this(session.getId(), session.getDataStart(), session.getDataEnd(), session.isOpen(), session.getTopic());
    }
}
