package com.desafiovotacaoapi.desafiovotacaoapi.repository;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByTopicId(Long id);

}
