package br.edu.felipebueno.arcade.domain.model;

public class Estoque {

    public boolean possuiDisponibilidade(Produto produto, int quantidade) {
        return produto != null && produto.possuiEstoque(quantidade);
    }

    public void reservar(Produto produto, int quantidade) {
        produto.removerEstoque(quantidade);
    }

    public void devolver(Produto produto, int quantidade) {
        produto.adicionarEstoque(quantidade);
    }
}
