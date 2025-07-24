package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para informações de estoque de um produto.")
public class EstoqueDto {

    @Schema(description = "ID do produto relacionado ao estoque", example = "1")
    private Integer idProduto;

    @Schema(description = "Quantidade disponível em estoque", example = "50")
    private Integer quantidadeEstoque;

    @Schema(description = "SKU do produto", example = "AP-IPH-001")
    private String skuProduto;
}
