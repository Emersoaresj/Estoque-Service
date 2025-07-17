package com.fiap.postech.estoque_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizaEstoqueRequest {
    private Integer idProduto;
    private Integer novaQuantidade;
}
