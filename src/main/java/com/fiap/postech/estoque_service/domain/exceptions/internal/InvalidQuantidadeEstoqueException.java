package com.fiap.postech.estoque_service.domain.exceptions.internal;

public class InvalidQuantidadeEstoqueException extends RuntimeException {
  public InvalidQuantidadeEstoqueException(String message) {
    super(message);
  }
}
