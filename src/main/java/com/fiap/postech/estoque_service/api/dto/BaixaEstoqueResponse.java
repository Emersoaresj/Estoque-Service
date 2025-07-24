package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta do endpoint de baixa de estoque")
public class BaixaEstoqueResponse {

    @Schema(description = "Indica se a baixa foi realizada com sucesso", example = "true")
    private boolean sucesso;

    @Schema(description = "Mensagem de retorno da operação", example = "Baixa realizada com sucesso!")
    private String mensagem;
}
