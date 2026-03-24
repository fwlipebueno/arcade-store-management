package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemVenda {
    private final Produto produto;
    private final int quantidade;
    private final BigDecimal precoUnitario;

    public ItemVenda(Produto produto, int quantidade, BigDecimal precoUnitario) {
        this.produto = Objects.requireNonNull(produto, "O produto do item é obrigatório.");

        if (quantidade <= 0) {
            throw new BusinessException("A quantidade do item deve ser maior que zero.");
        }

        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço unitário deve ser maior que zero.");
        }

        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}
