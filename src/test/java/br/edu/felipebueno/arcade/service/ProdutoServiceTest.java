package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Categoria;
import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.repository.memory.InMemoryProdutoRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoServiceTest {

    @Test
    void deveCadastrarProdutoComSucesso() {
        ProdutoService produtoService = new ProdutoService(new InMemoryProdutoRepository());

        Produto produto = produtoService.cadastrar(
                "Controle Arcade",
                "Controle USB para jogos",
                new BigDecimal("199.90"),
                8,
                new Categoria(1L, "Periféricos")
        );

        assertEquals(1L, produto.getId());
        assertEquals("Controle Arcade", produto.getNome());
        assertEquals(8, produto.getQuantidadeEstoque());
    }

    @Test
    void deveImpedirCadastroComPrecoInvalido() {
        ProdutoService produtoService = new ProdutoService(new InMemoryProdutoRepository());

        assertThrows(BusinessException.class, () -> produtoService.cadastrar(
                "Controle Arcade",
                "Controle USB para jogos",
                BigDecimal.ZERO,
                8,
                new Categoria(1L, "Periféricos")
        ));
    }
}
