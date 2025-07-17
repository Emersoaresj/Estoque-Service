package com.fiap.postech.estoque_service.gateway.port;

import com.fiap.postech.estoque_service.api.dto.*;

public interface EstoqueServicePort {

    ResponseDto cadastrarEstoque(EstoqueRequest request);

    ResponseDto atualizarEstoque(AtualizaEstoqueRequest request);

    BaixaEstoqueResponse baixarEstoque(BaixaEstoqueRequest request);

    BaixaEstoqueResponse restaurarEstoque(BaixaEstoqueRequest request);

}
