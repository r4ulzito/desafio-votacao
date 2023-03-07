package com.desafiovotacaoapi.desafiovotacaoapi.service;

import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.CreateSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.GetSessionDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.sessionDto.SessionVoteRequestDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.dto.voteDto.CreateVoteDTO;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.NullQueryResultExcepetion;
import com.desafiovotacaoapi.desafiovotacaoapi.exception.SessionClosedException;
import com.desafiovotacaoapi.desafiovotacaoapi.mapper.SessionMapper;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Associate;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import com.desafiovotacaoapi.desafiovotacaoapi.repository.SessionRepository;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateNewSessionEndDate;
import com.desafiovotacaoapi.desafiovotacaoapi.validation.ValidateVoteAssociate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AssociateService associateService;
    private final TopicService topicService;
    private final VoteService voteService;

    @Autowired
    public SessionService(SessionRepository sessionRepository, AssociateService associateService, TopicService topicService, VoteService voteService) {
        this.sessionRepository = sessionRepository;
        this.associateService = associateService;
        this.topicService = topicService;
        this.voteService = voteService;
    }

    public Session createSession(CreateSessionDTO sessionDTO) {

        Topic targetTopic = this.topicService.getTopicById(sessionDTO.topic_id());

        LocalDateTime dataEnd = ValidateNewSessionEndDate.validateEndDate(sessionDTO.data_end());

        return this.sessionRepository.save(SessionMapper.buildSession(dataEnd, targetTopic));

    }

    public List<GetSessionDTO> getAllSessions() {

        return this.sessionRepository.findAll().stream().map(GetSessionDTO::new).toList();
    }

    public Session getSessionById(Long sessionId) {

        return this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NullQueryResultExcepetion("Session not found!"));

    }

    public Vote newVote(SessionVoteRequestDTO newSessionVote) {

        Session targetSession = this.getSessionById(newSessionVote.session_id());

        if (!targetSession.isOpen() || LocalDateTime.now().isAfter(targetSession.getDataEnd())) {
            targetSession.setOpen(false);
            this.sessionRepository.save(targetSession);
            throw new SessionClosedException("Session is closed!");
        }

        Associate targetAssociate = this.associateService.getAssociateByID(newSessionVote.associate_id());
        Topic targetTopic = this.topicService.getTopicById(targetSession.getTopic().getId());

        List<Vote> targetTopicVotes = this.voteService.getAllByTopicId(targetTopic.getId());

        ValidateVoteAssociate.validateAssociateCanVote(targetTopicVotes, targetAssociate);

        return this.voteService.createVote(new CreateVoteDTO(newSessionVote.answer(), targetAssociate, targetTopic));
    }

}
