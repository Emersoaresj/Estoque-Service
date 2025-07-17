package com.fiap.postech.estoque_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaixaEstoqueResponse {
    private boolean sucesso;
    private String mensagem;
}
