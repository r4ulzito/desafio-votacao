package com.desafiovotacaoapi.desafiovotacaoapi.service.impl;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.InvalidTopicException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultException;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.SessionClosedException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.SessionMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.SessionRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.service.AssociateService;
import com.desafiovotacaoapi.desafiovotacaoapi.service.SessionService;
import com.desafiovotacaoapi.desafiovotacaoapi.service.TopicService;
import com.desafiovotacaoapi.desafiovotacaoapi.service.VoteService;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateNewSessionEndDate;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateVoteAssociate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionServiceImplements implements SessionService {

    private final SessionRepository sessionRepository;
    private final AssociateService associateService;
    private final TopicService topicService;
    private final VoteService voteService;

    @Autowired
    public SessionServiceImplements(
            SessionRepository sessionRepository,
            AssociateService associateService,
            TopicService topicService,
            VoteService voteService
    ) {
        this.sessionRepository = sessionRepository;
        this.associateService = associateService;
        this.topicService = topicService;
        this.voteService = voteService;
    }

    public Session createSession(CreateSessionDTO sessionDTO) {

        Topic targetTopic = this.topicService.getTopicById(sessionDTO.topic_id());

        List<Session> topicSessions = sessionRepository.findAllByTopicId(targetTopic.getId());

        topicSessions.forEach(session -> {
            if (!this.sessionIsClosed(session)) {
                throw new InvalidTopicException("Already exist a open session for this topic!");
            }
        });

        LocalDateTime dataEnd = ValidateNewSessionEndDate.validateEndDate(sessionDTO.data_end());

        return this.sessionRepository.save(SessionMapper.buildSession(dataEnd, targetTopic));

    }

    public List<GetSessionDTO> getAllSessions() {

        List<Session> allSessions = this.sessionRepository.findAll();

        allSessions.forEach(this::sessionIsClosed);

        return allSessions.stream().map(GetSessionDTO::new).toList();
    }

    public Session getSessionById(Long sessionId) {

        return this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NullQueryResultException("Session not found!"));
    }

    public Vote newVote(SessionVoteRequestDTO sessionVoteRequestDTO) {

        Session targetSession = this.getSessionById(sessionVoteRequestDTO.session_id());

        if (this.sessionIsClosed(targetSession)) {
            throw new SessionClosedException("Session is closed!");
        }

        Associate targetAssociate = this.associateService.getAssociateByID(sessionVoteRequestDTO.associate_id());
        Topic targetTopic = this.topicService.getTopicById(targetSession.getTopic().getId());

        List<Vote> targetTopicVotes = this.voteService.getAllByTopicId(targetTopic.getId());

        ValidateVoteAssociate.validateAssociateCanVote(targetTopicVotes, targetAssociate);

        return this.voteService.createVote(new CreateVoteDTO(sessionVoteRequestDTO.answer(), targetAssociate, targetTopic));
    }

    private boolean sessionIsClosed(Session session) {

        if (!session.isOpen()) {
            return true;
        } else if (LocalDateTime.now().isAfter(session.getDataEnd())) {
            session.setOpen(false);
            this.sessionRepository.save(session);
            return true;
        }

        return false;
    }

}
