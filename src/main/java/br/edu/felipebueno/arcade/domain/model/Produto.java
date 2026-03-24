package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public class Produto {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidadeEstoque;
    private Categoria categoria;

    public Produto(Long id, String nome, String descricao, BigDecimal preco, int quantidadeEstoque, Categoria categoria) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "O nome do produto é obrigatório.").trim();
        this.descricao = descricao == null ? null : descricao.trim();
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
        this.categoria = Objects.requireNonNull(categoria, "A categoria do produto é obrigatória.");
    }

    public void adicionarEstoque(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidadeEstoque += quantidade;
    }

    public void removerEstoque(int quantidade) {
        validarQuantidade(quantidade);

        if (quantidade > this.quantidadeEstoque) {
            throw new BusinessException("Estoque insuficiente para o produto " + nome + ".");
        }

        this.quantidadeEstoque -= quantidade;
    }

    public boolean possuiEstoque(int quantidade) {
        return quantidade > 0 && quantidade <= quantidadeEstoque;
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new BusinessException("A quantidade deve ser maior que zero.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome do produto é obrigatório.").trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao == null ? null : descricao.trim();
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço do produto deve ser maior que zero.");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new BusinessException("A quantidade em estoque não pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = Objects.requireNonNull(categoria, "A categoria do produto é obrigatória.");
    }
}
