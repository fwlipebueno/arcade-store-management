package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.service.ClienteService;
import br.edu.felipebueno.arcade.service.ProdutoService;
import br.edu.felipebueno.arcade.service.VendaService;

public class SistemaLojaArcade {
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final VendaService vendaService;

    public SistemaLojaArcade(ProdutoService produtoService, ClienteService clienteService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.vendaService = vendaService;
    }

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public VendaService getVendaService() {
        return vendaService;
    }
}
