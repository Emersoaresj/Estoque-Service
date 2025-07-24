package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Requisição para dar baixa em itens do estoque")
public class BaixaEstoqueRequest {

    @Schema(
            description = "Lista de itens para baixa",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[{\"idProduto\": 1, \"quantidade\": 2}, {\"idProduto\": 2, \"quantidade\": 5}]"
    )
    private List<ItemEstoqueBaixaDTO> itens;
}
