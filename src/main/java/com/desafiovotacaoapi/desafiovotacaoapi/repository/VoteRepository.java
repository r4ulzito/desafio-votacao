package com.desafiovotacaoapi.desafiovotacaoapi.repository;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByTopicId(Long id);
}

