package com.fiap.postech.estoque_service.domain.exceptions;

public class ErroInternoException extends RuntimeException {
    public ErroInternoException(String message) {
        super(message);
    }
}
