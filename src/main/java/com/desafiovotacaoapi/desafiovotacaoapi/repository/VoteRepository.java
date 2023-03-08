package com.desafiovotacaoapi.desafiovotacaoapi.repository;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByTopicId(Long id);

}

