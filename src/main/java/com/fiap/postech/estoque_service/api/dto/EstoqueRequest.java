package com.fiap.postech.estoque_service.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstoqueRequest {

    @NotBlank(message = "O SKU do produto é obrigatório")
    private String skuProduto;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer quantidadeEstoque;
}
