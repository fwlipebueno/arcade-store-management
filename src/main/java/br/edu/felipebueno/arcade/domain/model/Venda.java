package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Venda {
    private Long id;
    private final Cliente cliente;
    private final LocalDateTime dataHora;
    private final List<ItemVenda> itens;

    public Venda(Long id, Cliente cliente) {
        this.id = id;
        this.cliente = Objects.requireNonNull(cliente, "O cliente da venda é obrigatório.");
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemVenda item) {
        if (item == null) {
            throw new BusinessException("O item da venda é obrigatório.");
        }

        itens.add(item);
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemVenda::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validarFechamento() {
        if (itens.isEmpty()) {
            throw new BusinessException("A venda deve possuir ao menos um item.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }
}
