package com.fiap.postech.estoque_service.gateway.port;

import com.fiap.postech.estoque_service.api.dto.ResponseDto;
import com.fiap.postech.estoque_service.domain.model.Estoque;

public interface EstoqueRepositoryPort {

    ResponseDto cadastrarEstoque(Estoque estoque);

    boolean existeEstoquePorIdProduto(Integer idProduto);

    Estoque buscarPorIdProduto(Integer idProduto);

    ResponseDto atualizarEstoque(Estoque estoque);

}
