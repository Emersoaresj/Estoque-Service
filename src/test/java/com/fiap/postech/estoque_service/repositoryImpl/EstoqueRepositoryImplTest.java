package com.fiap.postech.estoque_service.repositoryImpl;

import com.fiap.postech.estoque_service.api.dto.EstoqueDto;
import com.fiap.postech.estoque_service.api.dto.ResponseDto;
import com.fiap.postech.estoque_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.EstoqueNotFoundException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.ProdutoNotFoundException;
import com.fiap.postech.estoque_service.domain.model.Estoque;
import com.fiap.postech.estoque_service.gateway.database.EstoqueRepositoryImpl;
import com.fiap.postech.estoque_service.gateway.database.entity.EstoqueEntity;
import com.fiap.postech.estoque_service.gateway.database.repository.EstoqueRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueRepositoryImplTest {

    @InjectMocks
    private EstoqueRepositoryImpl repositoryImpl;

    @Mock
    private EstoqueRepositoryJPA estoqueRepositoryJPA;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- cadastrarEstoque ----------

    @Test
    void cadastrarEstoque_sucesso() {
        Estoque estoque = new Estoque(null, 10, "AP-IPH-001", 5);
        EstoqueEntity entity = new EstoqueEntity();
        entity.setIdProduto(10);
        entity.setSkuProduto("AP-IPH-001");
        entity.setQuantidadeEstoque(5);

        when(estoqueRepositoryJPA.save(any())).thenReturn(entity);

        ResponseDto resp = repositoryImpl.cadastrarEstoque(estoque);

        assertNotNull(resp);
        assertNotNull(resp.getMessage());
        assertTrue(resp.getMessage().contains("cadastrado"));
        assertTrue(resp.getData().toString().contains("AP-IPH-001"));
    }

    @Test
    void cadastrarEstoque_erroBancoLancaErroInterno() {
        Estoque estoque = new Estoque(null, 10, "AP-IPH-001", 5);
        when(estoqueRepositoryJPA.save(any())).thenThrow(new RuntimeException("erro db"));

        assertThrows(ErroInternoException.class, () -> repositoryImpl.cadastrarEstoque(estoque));
    }

    // ---------- buscarPorIdProduto ----------

    @Test
    void buscarPorIdProduto_sucesso() {
        EstoqueEntity entity = new EstoqueEntity();
        entity.setIdProduto(12);
        entity.setSkuProduto("SKU-123");
        entity.setQuantidadeEstoque(3);

        when(estoqueRepositoryJPA.findByIdProduto(12)).thenReturn(Optional.of(entity));

        Estoque estoque = repositoryImpl.buscarPorIdProduto(12);

        assertNotNull(estoque);
        assertEquals(12, estoque.getIdProduto());
        assertEquals("SKU-123", estoque.getSkuProduto());
        assertEquals(3, estoque.getQuantidadeEstoque());
    }

    @Test
    void buscarPorIdProduto_produtoNotFound() {
        when(estoqueRepositoryJPA.findByIdProduto(44)).thenReturn(Optional.empty());

        assertThrows(ErroInternoException.class, () -> repositoryImpl.buscarPorIdProduto(44));
    }

    @Test
    void buscarPorIdProduto_erroBancoLancaErroInterno() {
        when(estoqueRepositoryJPA.findByIdProduto(any())).thenThrow(new RuntimeException("erro db"));

        assertThrows(ErroInternoException.class, () -> repositoryImpl.buscarPorIdProduto(3));
    }

    // ---------- atualizarEstoque ----------

    @Test
    void atualizarEstoque_sucesso() {
        Estoque estoque = new Estoque(1, 10, "AP-IPH-002", 8);
        EstoqueEntity entity = new EstoqueEntity();
        entity.setIdEstoque(1);
        entity.setIdProduto(10);
        entity.setSkuProduto("AP-IPH-002");
        entity.setQuantidadeEstoque(8);

        when(estoqueRepositoryJPA.save(any())).thenReturn(entity);

        ResponseDto resp = repositoryImpl.atualizarEstoque(estoque);

        assertNotNull(resp);
        assertNotNull(resp.getMessage());
        assertTrue(resp.getMessage().contains("atualizado"));
        assertTrue(resp.getData().toString().contains("AP-IPH-002"));
    }

    @Test
    void atualizarEstoque_erroBancoLancaErroInterno() {
        Estoque estoque = new Estoque(1, 10, "AP-IPH-002", 8);
        when(estoqueRepositoryJPA.save(any())).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> repositoryImpl.atualizarEstoque(estoque));
    }

    // ---------- buscarPorSku ----------

    @Test
    void buscarPorSku_sucesso() {
        EstoqueEntity entity = new EstoqueEntity();
        entity.setIdProduto(15);
        entity.setSkuProduto("SKU-15");
        entity.setQuantidadeEstoque(9);

        when(estoqueRepositoryJPA.findBySkuProduto("SKU-15")).thenReturn(Optional.of(entity));

        Estoque estoque = repositoryImpl.buscarPorSku("SKU-15");

        assertNotNull(estoque);
        assertEquals("SKU-15", estoque.getSkuProduto());
        assertEquals(9, estoque.getQuantidadeEstoque());
    }

    @Test
    void buscarPorSku_notFound() {
        when(estoqueRepositoryJPA.findBySkuProduto("NOT-FOUND")).thenReturn(Optional.empty());

        assertThrows(EstoqueNotFoundException.class, () -> repositoryImpl.buscarPorSku("NOT-FOUND"));
    }

    // ---------- listarTodos ----------

    @Test
    void listarTodos_sucesso() {
        EstoqueEntity entity1 = new EstoqueEntity();
        entity1.setIdProduto(1);
        entity1.setSkuProduto("SKU-1");
        entity1.setQuantidadeEstoque(5);

        EstoqueEntity entity2 = new EstoqueEntity();
        entity2.setIdProduto(2);
        entity2.setSkuProduto("SKU-2");
        entity2.setQuantidadeEstoque(7);

        List<EstoqueEntity> entityList = Arrays.asList(entity1, entity2);

        when(estoqueRepositoryJPA.findAll()).thenReturn(entityList);

        List<EstoqueDto> lista = repositoryImpl.listarTodos();

        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(2, lista.size());
        assertEquals("SKU-1", lista.get(0).getSkuProduto());
    }

    @Test
    void listarTodos_erroBancoLancaErroInterno() {
        when(estoqueRepositoryJPA.findAll()).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> repositoryImpl.listarTodos());
    }

    // ---------- deletarEstoque ----------

    @Test
    void deletarEstoque_sucesso() {
        EstoqueEntity entity = new EstoqueEntity();
        entity.setSkuProduto("DEL-001");

        when(estoqueRepositoryJPA.findBySkuProduto("DEL-001")).thenReturn(Optional.of(entity));
        doNothing().when(estoqueRepositoryJPA).deleteBySkuProduto("DEL-001");

        assertDoesNotThrow(() -> repositoryImpl.deletarEstoque("DEL-001"));
        verify(estoqueRepositoryJPA).deleteBySkuProduto("DEL-001");
    }

    @Test
    void deletarEstoque_notFound() {
        when(estoqueRepositoryJPA.findBySkuProduto("NOT-FOUND")).thenReturn(Optional.empty());

        assertThrows(EstoqueNotFoundException.class, () -> repositoryImpl.deletarEstoque("NOT-FOUND"));
    }
}
