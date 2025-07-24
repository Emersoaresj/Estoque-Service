package com.fiap.postech.estoque_service.controller;

import com.fiap.postech.estoque_service.api.controller.EstoqueController;
import com.fiap.postech.estoque_service.api.dto.*;
import com.fiap.postech.estoque_service.gateway.port.EstoqueServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueControllerTest {

    @Mock
    private EstoqueServicePort service;

    @InjectMocks
    private EstoqueController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Teste cadastrarEstoque ---
    @Test
    void testCadastrarEstoque_ComSucesso() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(10);

        ResponseDto responseMock = new ResponseDto();
        responseMock.setMessage("Estoque cadastrado com sucesso!");
        responseMock.setData(null);

        when(service.cadastrarEstoque(request)).thenReturn(responseMock);

        ResponseEntity<ResponseDto> response = controller.cadastrarEstoque(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Estoque cadastrado com sucesso!", response.getBody().getMessage());
        verify(service).cadastrarEstoque(request);
    }

    // --- Teste baixarEstoque ---
    @Test
    void testBaixarEstoque_ComSucesso() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        BaixaEstoqueResponse responseMock = new BaixaEstoqueResponse(true, "Baixa realizada com sucesso!");
        when(service.baixarEstoque(request)).thenReturn(responseMock);

        ResponseEntity<BaixaEstoqueResponse> response = controller.baixarEstoque(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSucesso());
        assertEquals("Baixa realizada com sucesso!", response.getBody().getMensagem());
        verify(service).baixarEstoque(request);
    }

    @Test
    void testBaixarEstoque_EstoqueInsuficiente() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        BaixaEstoqueResponse responseMock = new BaixaEstoqueResponse(false, "Estoque insuficiente!");
        when(service.baixarEstoque(request)).thenReturn(responseMock);

        ResponseEntity<BaixaEstoqueResponse> response = controller.baixarEstoque(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isSucesso());
        assertEquals("Estoque insuficiente!", response.getBody().getMensagem());
    }

    // --- Teste atualizarEstoque ---
    @Test
    void testAtualizarEstoque_ComSucesso() {
        String sku = "AP-IPH-001";
        Integer novaQuantidade = 10;
        ResponseDto responseMock = new ResponseDto();
        responseMock.setMessage("Estoque atualizado com sucesso!");
        responseMock.setData(null);

        when(service.atualizarEstoque(sku, novaQuantidade)).thenReturn(responseMock);

        ResponseEntity<ResponseDto> response = controller.atualizarEstoque(sku, novaQuantidade);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estoque atualizado com sucesso!", response.getBody().getMessage());
        verify(service).atualizarEstoque(sku, novaQuantidade);
    }

    // --- Teste restaurarEstoque ---
    @Test
    void testRestaurarEstoque_ComSucesso() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        BaixaEstoqueResponse responseMock = new BaixaEstoqueResponse(true, "Restauracao realizada com sucesso!");
        when(service.restaurarEstoque(request)).thenReturn(responseMock);

        ResponseEntity<BaixaEstoqueResponse> response = controller.restaurarEstoque(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSucesso());
        assertEquals("Restauracao realizada com sucesso!", response.getBody().getMensagem());
        verify(service).restaurarEstoque(request);
    }

    // --- Teste buscarPorSku ---
    @Test
    void testBuscarPorSku_ComSucesso() {
        String sku = "AP-IPH-001";
        EstoqueDto estoqueMock = new EstoqueDto();
        estoqueMock.setIdProduto(1);
        estoqueMock.setSkuProduto(sku);
        estoqueMock.setQuantidadeEstoque(45);

        when(service.buscarPorSku(sku)).thenReturn(estoqueMock);

        ResponseEntity<EstoqueDto> response = controller.buscarPorSku(sku);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(estoqueMock, response.getBody());
        verify(service).buscarPorSku(sku);
    }

    // --- Teste listarTodos ---
    @Test
    void testListarTodos() {
        EstoqueDto estoque1 = new EstoqueDto();
        estoque1.setIdProduto(1);
        estoque1.setSkuProduto("AP-IPH-001");
        estoque1.setQuantidadeEstoque(45);

        EstoqueDto estoque2 = new EstoqueDto();
        estoque2.setIdProduto(2);
        estoque2.setSkuProduto("AP-IPH-002");
        estoque2.setQuantidadeEstoque(30);

        List<EstoqueDto> listaMock = Arrays.asList(estoque1, estoque2);
        when(service.listarTodos()).thenReturn(listaMock);

        ResponseEntity<List<EstoqueDto>> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service).listarTodos();
    }

    // --- Teste deletarEstoque ---
    @Test
    void testDeletarEstoque_ComSucesso() {
        String sku = "AP-IPH-001";
        doNothing().when(service).deletarEstoque(sku);

        ResponseEntity<Void> response = controller.deletarEstoque(sku);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deletarEstoque(sku);
    }

    // --- Exemplo de teste para exceção ---
    @Test
    void testBuscarPorSku_EstoqueNaoEncontrado() {
        String sku = "NOT-FOUND";
        when(service.buscarPorSku(sku)).thenThrow(new RuntimeException("Estoque não encontrado!"));

        assertThrows(RuntimeException.class, () -> controller.buscarPorSku(sku));
        verify(service).buscarPorSku(sku);
    }

}
