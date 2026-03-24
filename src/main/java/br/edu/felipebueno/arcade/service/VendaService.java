package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.domain.model.Estoque;
import br.edu.felipebueno.arcade.domain.model.ItemVenda;
import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.domain.model.Venda;
import br.edu.felipebueno.arcade.repository.ProdutoRepository;
import br.edu.felipebueno.arcade.repository.VendaRepository;

import java.util.List;

public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final Estoque estoque;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.estoque = new Estoque();
    }

    public Venda iniciarVenda(Cliente cliente) {
        return new Venda(null, cliente);
    }

    public void adicionarItem(Venda venda, Produto produto, int quantidade) {
        estoque.reservar(produto, quantidade);
        produtoRepository.save(produto);

        ItemVenda item = new ItemVenda(produto, quantidade, produto.getPreco());
        venda.adicionarItem(item);
    }

    public Venda finalizarVenda(Venda venda) {
        venda.validarFechamento();
        return vendaRepository.save(venda);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }
}
