package com.fiap.postech.estoque_service.api.mapper;

import com.fiap.postech.estoque_service.api.dto.EstoqueDto;
import com.fiap.postech.estoque_service.api.dto.EstoqueRequest;
import com.fiap.postech.estoque_service.domain.model.Estoque;
import com.fiap.postech.estoque_service.gateway.database.entity.EstoqueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EstoqueMapper {

    EstoqueMapper INSTANCE = Mappers.getMapper(EstoqueMapper.class);

    Estoque requestToDomain(EstoqueRequest request);

    @Mapping(target = "idEstoque", ignore = true)
    @Mapping(target = "skuProduto", source = "skuProduto")
    EstoqueEntity domainToEntity(Estoque estoque);


    Estoque entityToDomain(EstoqueEntity entity);

    EstoqueDto domainToDto (Estoque estoque);

    List<EstoqueDto> domainToDtoList(List<EstoqueEntity> estoques);

}
