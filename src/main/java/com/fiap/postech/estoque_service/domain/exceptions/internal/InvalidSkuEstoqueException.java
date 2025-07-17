package com.fiap.postech.estoque_service.domain.exceptions.internal;

public class InvalidSkuEstoqueException extends RuntimeException {
  public InvalidSkuEstoqueException(String message) {
    super(message);
  }
}
