package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Informações de um item para baixa de estoque")
public class ItemEstoqueBaixaDTO {

    @Schema(description = "ID do produto a ser baixado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idProduto;

    @Schema(description = "Quantidade a dar baixa", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantidade;
}
