package com.fiap.postech.estoque_service.service;

import com.fiap.postech.estoque_service.api.dto.*;
import com.fiap.postech.estoque_service.api.mapper.EstoqueMapper;
import com.fiap.postech.estoque_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.*;
import com.fiap.postech.estoque_service.domain.model.Estoque;
import com.fiap.postech.estoque_service.gateway.client.ProdutoClient;
import com.fiap.postech.estoque_service.gateway.client.dto.ProdutoDto;
import com.fiap.postech.estoque_service.gateway.port.EstoqueRepositoryPort;
import com.fiap.postech.estoque_service.gateway.port.EstoqueServicePort;
import com.fiap.postech.estoque_service.utils.ConstantUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EstoqueServiceImpl implements EstoqueServicePort {

    @Autowired
    private EstoqueRepositoryPort repositoryPort;

    @Autowired
    private ProdutoClient produtoClient;

    @Override
    public ResponseDto cadastrarEstoque(EstoqueRequest request) {
        try {
            if (repositoryPort.buscarPorSku(request.getSkuProduto()) != null) {
                log.warn("Estoque já cadastrado para o SKU: {}", request.getSkuProduto());
                throw new EstoqueExistsException("Estoque já cadastrado para o SKU: " + request.getSkuProduto());
            }

            Estoque estoque = EstoqueMapper.INSTANCE.requestToDomain(request);
            validaCriacaoEstoque(estoque);

            ProdutoDto produto = chamadaProdutoClient(estoque);
            estoque.setIdProduto(produto.getIdProduto());

            return repositoryPort.cadastrarEstoque(estoque);

        } catch (InvalidQuantidadeEstoqueException | InvalidSkuEstoqueException |
                 ProdutoNotFoundException | EstoqueExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao cadastrar estoque", e);
            throw new ErroInternoException("Erro interno ao tentar cadastrar estoque: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto atualizarEstoque(String sku, Integer novaQuantidade) {
        try {
            Estoque estoque = repositoryPort.buscarPorSku(sku);
            estoque.setQuantidadeEstoque(novaQuantidade);

            return repositoryPort.atualizarEstoque(estoque);

        } catch (EstoqueNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar estoque", e);
            throw new ErroInternoException("Erro interno ao tentar atualizar estoque: " + e.getMessage());
        }
    }

    @Override
    public BaixaEstoqueResponse baixarEstoque(BaixaEstoqueRequest request) {

        for (ItemEstoqueBaixaDTO item : request.getItens()) {
            if (!validaEstoque(item.getIdProduto(), item.getQuantidade())) {
                return new BaixaEstoqueResponse(false, "Estoque insuficiente para o produto ID: " + item.getIdProduto());
            }
        }

        // Se todos têm estoque, baixa todos de uma vez
        for (ItemEstoqueBaixaDTO item : request.getItens()) {
            Estoque estoque = repositoryPort.buscarPorIdProduto(item.getIdProduto());
            estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() - item.getQuantidade());
            repositoryPort.atualizarEstoque(estoque);
        }

        return new BaixaEstoqueResponse(true, "Estoque baixado com sucesso");
    }

    @Override
    public BaixaEstoqueResponse restaurarEstoque(BaixaEstoqueRequest request) {

        for (ItemEstoqueBaixaDTO item : request.getItens()) {
            Estoque estoque = repositoryPort.buscarPorIdProduto(item.getIdProduto());
            if (estoque == null) {
                return new BaixaEstoqueResponse(false, "Estoque não encontrado para o produto ID: " + item.getIdProduto());
            }
            estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() + item.getQuantidade());
            repositoryPort.atualizarEstoque(estoque);
        }
        return new BaixaEstoqueResponse(true, "Estoque restaurado com sucesso");
    }

    @Override
    public EstoqueDto buscarPorSku(String skuProduto) {
        try {
            Estoque estoque = repositoryPort.buscarPorSku(skuProduto);
            return EstoqueMapper.INSTANCE.domainToDto(estoque);
        } catch (EstoqueNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar estoque por SKU", e);
            throw new ErroInternoException("Erro interno ao tentar buscar estoque: " + e.getMessage());
        }
    }

    @Override
    public List<EstoqueDto> listarTodos() {
        try {
            return repositoryPort.listarTodos();
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar estoques", e);
            throw new ErroInternoException("Erro interno ao tentar buscar estoques: " + e.getMessage());
        }
    }

    @Override
    public void deletarEstoque(String skuProduto) {
        try {
            repositoryPort.deletarEstoque(skuProduto);
        } catch (EstoqueNotFoundException e) {
            log.error("Estoque não encontrado para o SKU: {}", skuProduto, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar estoque por SKU: {}", skuProduto, e);
            throw new ErroInternoException("Erro interno ao tentar buscar produto: " + e.getMessage());
        }
    }

    private boolean validaEstoque(Integer idProduto, Integer quantidade) {
        // Se o estoque for nulo ou a quantidade for insuficiente, retorna false
        Estoque estoque = repositoryPort.buscarPorIdProduto(idProduto);
        return estoque != null && estoque.getQuantidadeEstoque() >= quantidade;
    }

    private void validaCriacaoEstoque(Estoque estoque) {
        if (!estoque.quantidadeValida()) {
            log.warn("Quantidade inválida para o SKU: {}", estoque.getSkuProduto());
            throw new InvalidQuantidadeEstoqueException("Quantidade não pode ser negativa.");
        }
        if (!estoque.skuValido()) {
            log.warn("SKU inválido: {}", estoque.getSkuProduto());
            throw new InvalidSkuEstoqueException("SKU inválido. Deve seguir o padrão XX-XX-XXX.");
        }
    }


    private ProdutoDto chamadaProdutoClient(Estoque estoque) {
        try {
            return produtoClient.buscarPorSku(estoque.getSkuProduto());
        } catch (FeignException.NotFound e) {
            log.error("Produto não encontrado para o SKU: {}", estoque.getSkuProduto());
            throw new ProdutoNotFoundException(ConstantUtils.PRODUTO_NAO_ENCONTRADO);
        } catch (Exception e) {
            log.error("Erro ao chamar o serviço de produto: {}", e.getMessage());
            throw new ErroInternoException("Erro ao buscar produto: " + e.getMessage());
        }
    }

}
