package com.fiap.postech.estoque_service.domain.exceptions.internal;

public class EstoqueNotFoundException extends RuntimeException {
  public EstoqueNotFoundException(String message) {
    super(message);
  }
}
