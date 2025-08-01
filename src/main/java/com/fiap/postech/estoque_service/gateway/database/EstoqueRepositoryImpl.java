package com.fiap.postech.estoque_service.gateway.database;

import com.fiap.postech.estoque_service.api.dto.EstoqueDto;
import com.fiap.postech.estoque_service.api.dto.ResponseDto;
import com.fiap.postech.estoque_service.api.mapper.EstoqueMapper;
import com.fiap.postech.estoque_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.EstoqueNotFoundException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.estoque_service.domain.model.Estoque;
import com.fiap.postech.estoque_service.gateway.database.entity.EstoqueEntity;
import com.fiap.postech.estoque_service.gateway.database.repository.EstoqueRepositoryJPA;
import com.fiap.postech.estoque_service.gateway.port.EstoqueRepositoryPort;
import com.fiap.postech.estoque_service.utils.ConstantUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class EstoqueRepositoryImpl implements EstoqueRepositoryPort {

    @Autowired
    private EstoqueRepositoryJPA estoqueRepositoryJPA;

    @Transactional
    @Override
    public ResponseDto cadastrarEstoque(Estoque estoque) {
        try {
            EstoqueEntity entity = EstoqueMapper.INSTANCE.domainToEntity(estoque);
            EstoqueEntity saved = estoqueRepositoryJPA.save(entity);
            log.info("Cadastrando estoque para o SKU: {}", estoque.getSkuProduto());
            return montaResponse(saved, "cadastrar");
        } catch (Exception e) {
            log.error("Erro ao cadastrar estoque", e);
            throw new ErroInternoException("Erro ao cadastrar estoque: " + e.getMessage());
        }
    }


    @Override
    public Estoque buscarPorIdProduto(Integer idProduto) {
        try {
            EstoqueEntity estoqueEntity = estoqueRepositoryJPA.findByIdProduto(idProduto)
                    .orElseThrow(() -> new ProdutoNotFoundException(ConstantUtils.PRODUTO_NAO_ENCONTRADO));
            return EstoqueMapper.INSTANCE.entityToDomain(estoqueEntity);

        } catch (Exception e) {
            log.error("Erro ao buscar estoque por ID do produto: {}", idProduto, e);
            throw new ErroInternoException("Erro ao buscar estoque: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDto atualizarEstoque(Estoque estoque) {
        try {
            EstoqueEntity entity = EstoqueMapper.INSTANCE.domainToEntity(estoque);
            entity.setIdEstoque(estoque.getIdEstoque());
            entity.setIdProduto(estoque.getIdProduto());
            EstoqueEntity updated = estoqueRepositoryJPA.save(entity);

            log.info("Atualizando estoque para o SKU: {}", estoque.getSkuProduto());
            return montaResponse(updated, "atualizar");
        } catch (Exception e) {
            log.error("Erro ao atualizar estoque", e);
            throw new ErroInternoException("Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    @Override
    public Estoque buscarPorSku(String skuProduto) {
        EstoqueEntity estoqueEntity = estoqueRepositoryJPA.findBySkuProduto(skuProduto)
                .orElseThrow(() -> new EstoqueNotFoundException(ConstantUtils.ESTOQUE_NAO_ENCONTRADO));
        return EstoqueMapper.INSTANCE.entityToDomain(estoqueEntity);
    }

    @Override
    public List<EstoqueDto> listarTodos() {
        try {
            List<EstoqueEntity> estoques = estoqueRepositoryJPA.findAll();
            return EstoqueMapper.INSTANCE.domainToDtoList(estoques);
        } catch (Exception e) {
            log.error("Erro ao buscar estoques", e);
            throw new ErroInternoException("Erro ao buscar estoques no banco de dados: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deletarEstoque(String skuProduto) {
        estoqueRepositoryJPA.findBySkuProduto(skuProduto)
                .orElseThrow(() -> new EstoqueNotFoundException(ConstantUtils.ESTOQUE_NAO_ENCONTRADO));
        estoqueRepositoryJPA.deleteBySkuProduto(skuProduto);
    }

    @Override
    public boolean estoqueExistsBySku(String skuProduto) {
        try {
            return estoqueRepositoryJPA.existsBySkuProduto(skuProduto);
        } catch (Exception e) {
            log.error("Erro ao verificar se o estoque existe para o SKU: {}", skuProduto, e);
            throw new ErroInternoException("Erro ao verificar estoque: " + e.getMessage());
        }
    }

    private ResponseDto montaResponse(EstoqueEntity estoqueEntity, String tipoAcao) {
        ResponseDto response = new ResponseDto();

        if ("cadastrar".equals(tipoAcao)) {
            response.setMessage(ConstantUtils.ESTOQUE_CADASTRADO);
        } else if ("atualizar".equals(tipoAcao)) {
            response.setMessage(ConstantUtils.ESTOQUE_ATUALIZADO);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("skuProduto", estoqueEntity.getSkuProduto());
        data.put("quantidadeEstoque", estoqueEntity.getQuantidadeEstoque());

        response.setData(data);
        return response;
    }

}