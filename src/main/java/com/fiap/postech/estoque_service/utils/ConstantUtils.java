package com.fiap.postech.estoque_service.utils;

import lombok.Data;

@Data
public class ConstantUtils {




    private ConstantUtils() {
        throw new IllegalStateException("Classe Utilitária");
    }


    //ERROS
    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado para o SKU informado.";
    public static final String ESTOQUE_JA_EXISTE = "Já existe estoque cadastrado para o SKU informado.";
    public static final String ESTOQUE_NAO_ENCONTRADO = "Estoque não encontrado.";


    //SUCESSO
    public static final String ESTOQUE_CADASTRADO = "Estoque cadastrado com sucesso!";
    public static final String ESTOQUE_ATUALIZADO = "Estoque atualizado com sucesso!";
}
