package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Categoria;
import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(String nome, String descricao, BigDecimal preco, int quantidadeEstoque, Categoria categoria) {
        Produto produto = new Produto(null, nome, descricao, preco, quantidadeEstoque, categoria);
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long produtoId, String nome, String descricao, BigDecimal preco, Categoria categoria) {
        Produto produto = buscarPorId(produtoId);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    public void remover(Long produtoId) {
        buscarPorId(produtoId);
        produtoRepository.deleteById(produtoId);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado."));
    }

    public void adicionarEstoque(Long produtoId, int quantidade) {
        Produto produto = buscarPorId(produtoId);
        produto.adicionarEstoque(quantidade);
        produtoRepository.save(produto);
    }
}
