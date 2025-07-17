package com.fiap.postech.estoque_service.domain.exceptions.internal;

public class ProdutoNotFoundException extends RuntimeException {
  public ProdutoNotFoundException(String message) {
    super(message);
  }
}
