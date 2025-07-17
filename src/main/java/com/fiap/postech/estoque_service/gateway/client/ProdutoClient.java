package com.fiap.postech.estoque_service.gateway.client;

import com.fiap.postech.estoque_service.gateway.client.dto.ProdutoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "estoque-produto-service", url = "${produto.service.url}")
public interface ProdutoClient {

    @GetMapping("/api/produtos/sku/{sku}")
    ProdutoDto buscarPorSku(@PathVariable("sku") String sku);
}
