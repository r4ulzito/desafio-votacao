package com.desafiovotacaoapi.desafiovotacaoapi.model;

import jakarta.persistence.*;

public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "topic_id", nullable = false)
    @ManyToOne
    Topic topic;



}


