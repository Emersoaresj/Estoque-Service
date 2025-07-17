package com.fiap.postech.estoque_service.domain.exceptions.internal;

public class EstoqueExistsException extends RuntimeException {
  public EstoqueExistsException(String message) {
    super(message);
  }
}
