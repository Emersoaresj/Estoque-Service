package com.fiap.postech.estoque_service.gateway.port;

import com.fiap.postech.estoque_service.api.dto.EstoqueDto;
import com.fiap.postech.estoque_service.api.dto.ResponseDto;
import com.fiap.postech.estoque_service.domain.model.Estoque;

import java.util.List;

public interface EstoqueRepositoryPort {

    ResponseDto cadastrarEstoque(Estoque estoque);

    Estoque buscarPorIdProduto(Integer idProduto);

    ResponseDto atualizarEstoque(Estoque estoque);

    Estoque buscarPorSku(String skuProduto);

    List<EstoqueDto> listarTodos();

    void deletarEstoque(String skuProduto);

}
