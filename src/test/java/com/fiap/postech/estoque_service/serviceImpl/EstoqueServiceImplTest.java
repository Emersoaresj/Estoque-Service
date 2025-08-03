package com.fiap.postech.estoque_service.serviceImpl;


import com.fiap.postech.estoque_service.api.dto.*;
import com.fiap.postech.estoque_service.api.mapper.EstoqueMapper;
import com.fiap.postech.estoque_service.domain.exceptions.ErroInternoException;
import com.fiap.postech.estoque_service.domain.exceptions.internal.*;
import com.fiap.postech.estoque_service.domain.model.Estoque;
import com.fiap.postech.estoque_service.gateway.client.ProdutoClient;
import com.fiap.postech.estoque_service.gateway.client.dto.ProdutoDto;
import com.fiap.postech.estoque_service.gateway.port.EstoqueRepositoryPort;
import com.fiap.postech.estoque_service.service.EstoqueServiceImpl;
import com.fiap.postech.estoque_service.utils.ConstantUtils;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.yaml.snakeyaml.scanner.Constant;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceImplTest {

    @InjectMocks
    private EstoqueServiceImpl service;

    @Mock
    private EstoqueRepositoryPort repositoryPort;
    @Mock
    private ProdutoClient produtoClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- cadastrarEstoque ----------

    @Test
    void cadastrarEstoque_sucesso() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(5);

        // O estoque ainda não existe
        when(repositoryPort.buscarPorSku("AP-IPH-001")).thenReturn(null);

        Estoque estoque = new Estoque(null, null, "AP-IPH-001", 5);

        when(repositoryPort.cadastrarEstoque(any())).thenReturn(new ResponseDto());

        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setIdProduto(99);
        produtoDto.setSkuProduto("AP-IPH-001");
        produtoDto.setNomeProduto("IPHONE");
        produtoDto.setPrecoProduto(BigDecimal.valueOf(2000));

        when(produtoClient.buscarPorSku("AP-IPH-001")).thenReturn(produtoDto);

        ResponseDto resp = service.cadastrarEstoque(request);

        assertNotNull(resp);
        verify(repositoryPort).cadastrarEstoque(any(Estoque.class));

    }

    @Test
    void cadastrarEstoque_deveLancarEstoqueExistsException() {
        // Arrange
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(1);

        // Mockando que o estoque já existe para o SKU
        when(repositoryPort.estoqueExistsBySku("AP-IPH-001")).thenReturn(true);

        // Act & Assert
        assertThrows(EstoqueExistsException.class, () -> service.cadastrarEstoque(request));

        // Optional: você pode verificar que o método de client não foi chamado:
        verify(produtoClient, never()).buscarPorSku(anyString());
    }

    @Test
    void cadastrarEstoque_deveLancarInvalidQuantidadeEstoqueException() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(-5);

        when(repositoryPort.buscarPorSku("AP-IPH-001")).thenReturn(null);

        assertThrows(InvalidQuantidadeEstoqueException.class, () -> service.cadastrarEstoque(request));
    }


    @Test
    void cadastrarEstoque_deveLancarInvalidSkuEstoqueException() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("invalido  ");
        request.setQuantidadeEstoque(1);

        Estoque estoque = new Estoque(null, null, "invalido", 1);

        when(repositoryPort.buscarPorSku(any())).thenReturn(null);

        assertThrows(InvalidSkuEstoqueException.class, () -> service.cadastrarEstoque(request));
    }

    @Test
    void cadastrarEstoque_deveLancarProdutoNotFoundException() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(2);

        when(repositoryPort.buscarPorSku("AP-IPH-001")).thenReturn(null);

        // Cria um Request dummy só para o FeignException
        Request feignRequest = Request.create(
                Request.HttpMethod.GET,
                "http://fake",
                Collections.emptyMap(),
                null,
                null,
                null
        );

        FeignException notFound = new FeignException.NotFound(
                "Produto não encontrado",
                feignRequest,
                null,
                Collections.emptyMap()
        );

        when(produtoClient.buscarPorSku("AP-IPH-001")).thenThrow(notFound);

        assertThrows(ProdutoNotFoundException.class, () -> service.cadastrarEstoque(request));
    }

    @Test
    void cadastrarEstoque_deveLancarErroInternoException() {
        EstoqueRequest request = new EstoqueRequest();
        request.setSkuProduto("AP-IPH-001");
        request.setQuantidadeEstoque(1);

        when(repositoryPort.buscarPorSku(any())).thenThrow(new RuntimeException("Erro inesperado"));

        assertThrows(ErroInternoException.class, () -> service.cadastrarEstoque(request));
    }

    // ---------- atualizarEstoque ----------

    @Test
    void atualizarEstoque_sucesso() {
        String sku = "AP-IPH-001";
        Integer novaQtd = 22;

        Estoque estoque = new Estoque(1, 2, sku, 10);
        when(repositoryPort.buscarPorSku(sku)).thenReturn(estoque);

        when(repositoryPort.atualizarEstoque(any())).thenReturn(new ResponseDto());

        ResponseDto resp = service.atualizarEstoque(sku, novaQtd);

        assertNotNull(resp);
        verify(repositoryPort).atualizarEstoque(estoque);
        assertEquals(novaQtd, estoque.getQuantidadeEstoque());
    }

    @Test
    void atualizarEstoque_deveLancarEstoqueNotFoundException() {
        String sku = "SKUNOTFOUND";
        when(repositoryPort.buscarPorSku(sku)).thenThrow(new EstoqueNotFoundException("not found"));

        assertThrows(EstoqueNotFoundException.class, () -> service.atualizarEstoque(sku, 10));
    }

    @Test
    void atualizarEstoque_deveLancarErroInternoException() {
        String sku = "AP-IPH-001";
        when(repositoryPort.buscarPorSku(sku)).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> service.atualizarEstoque(sku, 9));
    }

    // ---------- baixarEstoque ----------

    @Test
    void baixarEstoque_sucesso() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        ItemEstoqueBaixaDTO item = new ItemEstoqueBaixaDTO();
        item.setIdProduto(1);
        item.setQuantidade(2);
        request.setItens(Collections.singletonList(item));

        Estoque estoque = new Estoque(1, 1, "SKU-1", 5);

        when(repositoryPort.buscarPorIdProduto(1)).thenReturn(estoque);
        when(repositoryPort.atualizarEstoque(any())).thenReturn(new ResponseDto());

        BaixaEstoqueResponse resp = service.baixarEstoque(request);

        assertTrue(resp.isSucesso());
        assertEquals("Estoque baixado com sucesso", resp.getMensagem());
        verify(repositoryPort).atualizarEstoque(any());
    }

    @Test
    void baixarEstoque_comEstoqueInsuficiente() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        ItemEstoqueBaixaDTO item = new ItemEstoqueBaixaDTO();
        item.setIdProduto(1);
        item.setQuantidade(10); // insuficiente!
        request.setItens(Collections.singletonList(item));

        Estoque estoque = new Estoque(1, 1, "SKU-1", 5);

        when(repositoryPort.buscarPorIdProduto(1)).thenReturn(estoque);

        BaixaEstoqueResponse resp = service.baixarEstoque(request);

        assertFalse(resp.isSucesso());
        assertTrue(resp.getMensagem().contains("Estoque insuficiente"));
    }

    // ---------- restaurarEstoque ----------

    @Test
    void restaurarEstoque_sucesso() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        ItemEstoqueBaixaDTO item = new ItemEstoqueBaixaDTO();
        item.setIdProduto(2);
        item.setQuantidade(3);
        request.setItens(Collections.singletonList(item));

        Estoque estoque = new Estoque(10, 2, "SKU-2", 7);

        when(repositoryPort.buscarPorIdProduto(2)).thenReturn(estoque);
        when(repositoryPort.atualizarEstoque(any())).thenReturn(new ResponseDto());

        BaixaEstoqueResponse resp = service.restaurarEstoque(request);

        assertTrue(resp.isSucesso());
        assertEquals("Estoque restaurado com sucesso", resp.getMensagem());
        verify(repositoryPort).atualizarEstoque(any());
    }

    @Test
    void restaurarEstoque_estoqueNaoEncontrado() {
        BaixaEstoqueRequest request = new BaixaEstoqueRequest();
        ItemEstoqueBaixaDTO item = new ItemEstoqueBaixaDTO();
        item.setIdProduto(99);
        item.setQuantidade(1);
        request.setItens(Collections.singletonList(item));

        when(repositoryPort.buscarPorIdProduto(99)).thenReturn(null);

        BaixaEstoqueResponse resp = service.restaurarEstoque(request);

        assertFalse(resp.isSucesso());
        assertTrue(resp.getMensagem().contains("Estoque não encontrado"));
    }

    // ---------- buscarPorSku ----------

    @Test
    void buscarPorSku_sucesso() {
        String sku = "AP-IPH-001";
        Estoque estoque = new Estoque(1, 2, sku, 11);
        EstoqueDto dto = new EstoqueDto();
        dto.setIdProduto(2);
        dto.setSkuProduto(sku);
        dto.setQuantidadeEstoque(11);

        when(repositoryPort.buscarPorSku(sku)).thenReturn(estoque);

        EstoqueDto resp = service.buscarPorSku(sku);

        assertEquals(dto, resp);
    }

    @Test
    void buscarPorSku_deveLancarEstoqueNotFoundException() {
        String sku = "NOTFOUND";
        when(repositoryPort.buscarPorSku(sku)).thenThrow(new EstoqueNotFoundException("notfound"));

        assertThrows(EstoqueNotFoundException.class, () -> service.buscarPorSku(sku));
    }

    @Test
    void buscarPorSku_deveLancarErroInternoException() {
        String sku = "ERROR";
        when(repositoryPort.buscarPorSku(sku)).thenThrow(new RuntimeException("e"));

        assertThrows(ErroInternoException.class, () -> service.buscarPorSku(sku));
    }

    // ---------- listarTodos ----------

    @Test
    void listarTodos_sucesso() {
        List<EstoqueDto> list = new ArrayList<>();
        when(repositoryPort.listarTodos()).thenReturn(list);

        List<EstoqueDto> resp = service.listarTodos();

        assertSame(list, resp);
        verify(repositoryPort).listarTodos();
    }

    @Test
    void listarTodos_deveLancarErroInternoException() {
        when(repositoryPort.listarTodos()).thenThrow(new RuntimeException("erro"));

        assertThrows(ErroInternoException.class, () -> service.listarTodos());
    }

    // ---------- deletarEstoque ----------

    @Test
    void deletarEstoque_sucesso() {
        String sku = "AP-IPH-001";
        doNothing().when(repositoryPort).deletarEstoque(sku);

        assertDoesNotThrow(() -> service.deletarEstoque(sku));
        verify(repositoryPort).deletarEstoque(sku);
    }

    @Test
    void deletarEstoque_deveLancarEstoqueNotFoundException() {
        String sku = "NAOEXISTE";
        doThrow(new EstoqueNotFoundException("not found")).when(repositoryPort).deletarEstoque(sku);

        assertThrows(EstoqueNotFoundException.class, () -> service.deletarEstoque(sku));
    }

    @Test
    void deletarEstoque_deveLancarErroInternoException() {
        String sku = "ERRO";
        doThrow(new RuntimeException("erro")).when(repositoryPort).deletarEstoque(sku);

        assertThrows(ErroInternoException.class, () -> service.deletarEstoque(sku));
    }

    @Test
    void chamadaProdutoClient_deveLancarErroInternoException_paraErroGenerico() throws Exception {
        Estoque estoque = new Estoque();
        estoque.setSkuProduto("ERRO-GERAL");

        when(produtoClient.buscarPorSku("ERRO-GERAL"))
                .thenThrow(new RuntimeException("Falha inesperada no client"));

        java.lang.reflect.Method m = EstoqueServiceImpl.class.getDeclaredMethod("chamadaProdutoClient", Estoque.class);
        m.setAccessible(true);

        Exception exception = assertThrows(Exception.class, () -> m.invoke(service, estoque));

        // Agora, verifique se a causa é a ErroInternoException
        Throwable realException = exception.getCause();
        assertTrue(realException instanceof ErroInternoException);
        assertTrue(realException.getMessage().contains("Erro ao buscar produto"));
    }


}
