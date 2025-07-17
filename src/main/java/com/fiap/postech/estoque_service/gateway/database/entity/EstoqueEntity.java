package com.fiap.postech.estoque_service.gateway.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estoque")
@Data
public class EstoqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque")
    private Integer idEstoque;

    @Column(name = "id_produto", nullable = false, unique = true)
    private Integer idProduto; // FK para Produto

    @Column(name = "sku_produto", nullable = false, unique = true)
    private String skuProduto;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}
