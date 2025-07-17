package com.fiap.postech.estoque_service.gateway.database.repository;

import com.fiap.postech.estoque_service.gateway.database.entity.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepositoryJPA extends JpaRepository<EstoqueEntity, Integer> {

    boolean existsByIdProduto(Integer idProduto);

    Optional<EstoqueEntity> findByIdProduto(Integer idProduto);
}
