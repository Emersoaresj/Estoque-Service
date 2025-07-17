package com.fiap.postech.estoque_service.api.dto;

import lombok.Data;

@Data
public class ItemEstoqueBaixaDTO {
    private Integer idProduto;
    private Integer quantidade;
}
