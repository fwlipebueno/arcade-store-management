package br.edu.felipebueno.arcade.app.context;

import br.edu.felipebueno.arcade.domain.model.SistemaLojaArcade;
import br.edu.felipebueno.arcade.repository.memory.InMemoryClienteRepository;
import br.edu.felipebueno.arcade.repository.memory.InMemoryProdutoRepository;
import br.edu.felipebueno.arcade.repository.memory.InMemoryVendaRepository;
import br.edu.felipebueno.arcade.service.ClienteService;
import br.edu.felipebueno.arcade.service.ProdutoService;
import br.edu.felipebueno.arcade.service.VendaService;

public class ApplicationContext {
    public SistemaLojaArcade criarSistema() {
        InMemoryProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        InMemoryClienteRepository clienteRepository = new InMemoryClienteRepository();
        InMemoryVendaRepository vendaRepository = new InMemoryVendaRepository();

        ProdutoService produtoService = new ProdutoService(produtoRepository);
        ClienteService clienteService = new ClienteService(clienteRepository);
        VendaService vendaService = new VendaService(vendaRepository, produtoRepository);

        return new SistemaLojaArcade(produtoService, clienteService, vendaService);
    }
}
