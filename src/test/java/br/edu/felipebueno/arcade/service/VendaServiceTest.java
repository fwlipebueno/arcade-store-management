package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Categoria;
import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.domain.model.Venda;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.repository.memory.InMemoryProdutoRepository;
import br.edu.felipebueno.arcade.repository.memory.InMemoryVendaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VendaServiceTest {

    @Test
    void deveFinalizarVendaEBaixarEstoque() {
        InMemoryProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        InMemoryVendaRepository vendaRepository = new InMemoryVendaRepository();
        VendaService vendaService = new VendaService(vendaRepository, produtoRepository);

        Categoria categoria = new Categoria(1L, "Jogos");
        Produto produto = new Produto(null, "Controle Arcade", "Controle USB", new BigDecimal("199.90"), 5, categoria);
        produtoRepository.save(produto);

        Cliente cliente = new Cliente(null, "João", "123.456.789-00", null, null);
        Venda venda = vendaService.iniciarVenda(cliente);

        vendaService.adicionarItem(venda, produto, 2);
        Venda vendaFinalizada = vendaService.finalizarVenda(venda);

        assertEquals(new BigDecimal("399.80"), vendaFinalizada.calcularTotal());
        assertEquals(3, produto.getQuantidadeEstoque());
    }

    @Test
    void deveImpedirVendaSemEstoque() {
        InMemoryProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        InMemoryVendaRepository vendaRepository = new InMemoryVendaRepository();
        VendaService vendaService = new VendaService(vendaRepository, produtoRepository);

        Categoria categoria = new Categoria(1L, "Jogos");
        Produto produto = new Produto(null, "Ficha Arcade", "Pacote com fichas", new BigDecimal("10.00"), 1, categoria);
        Cliente cliente = new Cliente(null, "Lucas", "987.654.321-00", null, null);
        Venda venda = vendaService.iniciarVenda(cliente);

        assertThrows(BusinessException.class, () -> vendaService.adicionarItem(venda, produto, 2));
    }
}
