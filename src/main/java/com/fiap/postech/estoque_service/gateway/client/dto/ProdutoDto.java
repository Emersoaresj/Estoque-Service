package com.fiap.postech.estoque_service.gateway.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDto {

    private Integer idProduto;
    private String nomeProduto;
    private String skuProduto;
    private BigDecimal precoProduto;
}
