package com.fiap.postech.estoque_service.gateway.port;

import com.fiap.postech.estoque_service.api.dto.*;

import java.util.List;

public interface EstoqueServicePort {

    ResponseDto cadastrarEstoque(EstoqueRequest request);

    ResponseDto atualizarEstoque(String sku, Integer novaQuantidade);

    BaixaEstoqueResponse baixarEstoque(BaixaEstoqueRequest request);

    BaixaEstoqueResponse restaurarEstoque(BaixaEstoqueRequest request);

    EstoqueDto buscarPorSku(String skuProduto);

    List<EstoqueDto> listarTodos();

    void deletarEstoque(String skuProduto);

}
