package com.fiap.postech.estoque_service.api.controller;

import com.fiap.postech.estoque_service.api.dto.*;
import com.fiap.postech.estoque_service.gateway.port.EstoqueServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueServicePort service;


    @PostMapping("/cadastrar")
    public ResponseEntity<ResponseDto> cadastrarEstoque(@Valid @RequestBody EstoqueRequest request) {
        ResponseDto estoque = service.cadastrarEstoque(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @PostMapping("/baixa")
    public ResponseEntity<BaixaEstoqueResponse> baixarEstoque(@Valid @RequestBody BaixaEstoqueRequest request) {
        BaixaEstoqueResponse estoque = service.baixarEstoque(request);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ResponseDto> atualizarEstoque(@Valid @RequestBody AtualizaEstoqueRequest request) {
        ResponseDto estoque = service.atualizarEstoque(request);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @PostMapping("/restaurar")
    public ResponseEntity<BaixaEstoqueResponse> restaurarEstoque(@Valid @RequestBody BaixaEstoqueRequest request) {
        BaixaEstoqueResponse response = service.restaurarEstoque(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
