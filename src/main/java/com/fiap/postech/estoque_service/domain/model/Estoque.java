package com.fiap.postech.estoque_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    private Integer idEstoque;
    private Integer idProduto;
    private String skuProduto;
    private Integer quantidadeEstoque;

    public boolean quantidadeValida() {
        return quantidadeEstoque != null && quantidadeEstoque >= 0;
    }

    public boolean skuValido() {
        if (skuProduto == null) {
            return false;
        }
        skuProduto = skuProduto.trim().toUpperCase().replace(" ", "-");
        return skuProduto.matches("^[A-Z0-9]{2,}-[A-Z0-9]{2,}-[0-9]{3,}$");
    }
}
