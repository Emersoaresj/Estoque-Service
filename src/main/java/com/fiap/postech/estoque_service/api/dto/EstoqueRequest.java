package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Requisição para cadastro de novo estoque ou atualização de estoque de produto")
public class EstoqueRequest {

    @NotBlank(message = "O SKU do produto é obrigatório")
    @Schema(description = "SKU do produto a ser cadastrado ou atualizado no estoque", example = "AP-IPH-001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String skuProduto;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    @Schema(description = "Quantidade do produto em estoque", example = "10", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantidadeEstoque;
}
