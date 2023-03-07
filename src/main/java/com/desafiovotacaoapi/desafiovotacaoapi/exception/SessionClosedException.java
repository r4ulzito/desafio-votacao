package com.desafiovotacaoapi.desafiovotacaoapi.exception;

public class SessionClosedException extends RuntimeException {

    public SessionClosedException(String message) {
        super(message);
    }

}

