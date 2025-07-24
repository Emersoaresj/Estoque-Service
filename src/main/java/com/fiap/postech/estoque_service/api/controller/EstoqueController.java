package com.fiap.postech.estoque_service.api.controller;

import com.fiap.postech.estoque_service.api.dto.*;
import com.fiap.postech.estoque_service.gateway.port.EstoqueServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
@Tag(name = "Estoques", description = "Gerenciamento de Estoques")
public class EstoqueController {

    @Autowired
    private EstoqueServicePort service;

    @Operation(summary = "Cadastrar um novo estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estoque cadastrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Estoque cadastrado com sucesso!\"}"))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-24T10:00:00",
                                    "status": 400,
                                    "errors": {
                                        "skuProduto": "SKU obrigatório",
                                        "quantidade": "Quantidade obrigatória"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}"))),
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<ResponseDto> cadastrarEstoque(@Valid @RequestBody EstoqueRequest request) {
        ResponseDto estoque = service.cadastrarEstoque(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @Operation(summary = "Dar baixa em estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Baixa realizada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                  {
                                    "sucesso": true,
                                    "mensagem": "Baixa realizada com sucesso!"
                                  }
                            """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                {
                                  "sucesso": false,
                                  "mensagem": "Estoque insuficiente!"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                {
                                  "sucesso": false,
                                  "mensagem": "Erro interno!"
                                }
                            """))),
    })
    @PostMapping("/baixa")
    public ResponseEntity<BaixaEstoqueResponse> baixarEstoque(
            @Valid @RequestBody BaixaEstoqueRequest request) {
        BaixaEstoqueResponse response = service.baixarEstoque(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Atualizar a quantidade do estoque de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDto.class),
                    examples = @ExampleObject(value = "{\"mensagem\": \"Estoque atualizado com sucesso!\"}"))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2024-07-24T10:00:00",
                                    "status": 400,
                                    "errors": {
                                        "novaQuantidade": "Quantidade obrigatória e maior que zero"
                                    }
                                }
                            """))),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Estoque não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}"))),
    })
    @PutMapping("/atualizar/{skuProduto}")
    public ResponseEntity<ResponseDto> atualizarEstoque(
            @PathVariable String skuProduto,
            @RequestParam Integer novaQuantidade) {
        ResponseDto estoque = service.atualizarEstoque(skuProduto, novaQuantidade);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @Operation(summary = "Restaurar estoque após baixa indevida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque restaurado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                  {
                                    "sucesso": true,
                                    "mensagem": "Restauracao realizada com sucesso!"
                                  }
                            """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                {
                                  "sucesso": false,
                                  "mensagem": "Requisição inválida!"
                                }
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaixaEstoqueResponse.class),
                    examples = @ExampleObject(value = """
                                {
                                  "sucesso": false,
                                  "mensagem": "Erro interno!"
                                }
                            """))),
    })
    @PostMapping("/restaurar")
    public ResponseEntity<BaixaEstoqueResponse> restaurarEstoque(
            @Valid @RequestBody BaixaEstoqueRequest request) {
        BaixaEstoqueResponse response = service.restaurarEstoque(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Buscar estoque por SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estoque encontrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstoqueDto.class),
                    examples = @ExampleObject(value = """
                                  {
                                    "idProduto": 1,
                                    "skuProduto": "AP-IPH-001",
                                    "quantidade": 45
                                  }
                            """))),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Estoque não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}"))),
    })
    @GetMapping("/{skuProduto}")
    public ResponseEntity<EstoqueDto> buscarPorSku(@PathVariable String skuProduto) {
        EstoqueDto estoque = service.buscarPorSku(skuProduto);
        return ResponseEntity.status(HttpStatus.OK).body(estoque);
    }

    @Operation(summary = "Listar todos os estoques")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estoques retornada com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstoqueDto.class),
                    examples = @ExampleObject(value = """
                                    [
                                      {
                                        "idProduto": 1,
                                        "skuProduto": "AP-IPH-001",
                                        "quantidade": 45
                                      },
                                      {
                                        "idProduto": 2,
                                        "skuProduto": "AP-IPH-002",
                                        "quantidade": 30
                                      }
                                    ]
                            """))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}"))),
    })
    @GetMapping
    public ResponseEntity<List<EstoqueDto>> listarTodos() {
        List<EstoqueDto> estoques = service.listarTodos();
        return ResponseEntity.status(HttpStatus.OK).body(estoques);
    }

    @Operation(summary = "Deletar estoque pelo SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estoque deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estoque não encontrado", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Estoque não encontrado!\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno!\"}"))),
    })
    @DeleteMapping("/{skuProduto}")
    public ResponseEntity<Void> deletarEstoque(@PathVariable String skuProduto) {
        service.deletarEstoque(skuProduto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
