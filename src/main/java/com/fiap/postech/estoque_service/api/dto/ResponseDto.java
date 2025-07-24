package com.fiap.postech.estoque_service.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Resposta padrão das operações da API.")
public class ResponseDto {
    @Schema(description = "Mensagem da operação", example = "Produto cadastrado com sucesso!")
    private String message;

    @Schema(description = "Dados adicionais da resposta, se houver")
    private Object data;
}
