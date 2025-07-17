package com.fiap.postech.estoque_service.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class BaixaEstoqueRequest {
    private List<ItemEstoqueBaixaDTO> itens;
}
