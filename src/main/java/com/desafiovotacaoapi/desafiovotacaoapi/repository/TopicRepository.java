package com.desafiovotacaoapi.desafiovotacaoapi.repository;

import com.desafiovotacaoapi.desafiovotacaoapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
