package br.edu.felipebueno.arcade.app.console;

import br.edu.felipebueno.arcade.domain.model.Categoria;
import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.domain.model.SistemaLojaArcade;
import br.edu.felipebueno.arcade.domain.model.Venda;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.util.InputUtils;
import br.edu.felipebueno.arcade.util.OutputUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final SistemaLojaArcade sistema;
    private final Scanner scanner;
    private long categoriaSequence;

    public ConsoleMenu(SistemaLojaArcade sistema) {
        this.sistema = sistema;
        this.scanner = new Scanner(System.in);
        this.categoriaSequence = 1L;
    }

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = InputUtils.lerInteiro(scanner, "Escolha uma opção: ");
            System.out.println();

            try {
                switch (opcao) {
                    case 1 -> cadastrarProduto();
                    case 2 -> listarProdutos();
                    case 3 -> cadastrarCliente();
                    case 4 -> listarClientes();
                    case 5 -> realizarVenda();
                    case 6 -> listarVendas();
                    case 0 -> executando = false;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (BusinessException exception) {
                System.out.println("Erro de negócio: " + exception.getMessage());
            } catch (Exception exception) {
                System.out.println("Erro inesperado: " + exception.getMessage());
            }

            System.out.println();
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private void exibirMenu() {
        System.out.println("==============================");
        System.out.println(" Arcade Store Management ");
        System.out.println("==============================");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Cadastrar cliente");
        System.out.println("4 - Listar clientes");
        System.out.println("5 - Realizar venda");
        System.out.println("6 - Listar vendas");
        System.out.println("0 - Sair");
    }

    private void cadastrarProduto() {
        String nome = InputUtils.lerTexto(scanner, "Nome do produto: ");
        String descricao = InputUtils.lerTexto(scanner, "Descrição do produto: ");
        BigDecimal preco = InputUtils.lerBigDecimal(scanner, "Preço do produto: ");
        int quantidade = InputUtils.lerInteiro(scanner, "Quantidade em estoque: ");
        String categoriaNome = InputUtils.lerTexto(scanner, "Categoria: ");

        Categoria categoria = new Categoria(categoriaSequence++, categoriaNome);
        Produto produto = sistema.getProdutoService().cadastrar(nome, descricao, preco, quantidade, categoria);

        System.out.println("Produto cadastrado com sucesso. ID: " + produto.getId());
    }

    private void listarProdutos() {
        List<Produto> produtos = sistema.getProdutoService().listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("Produtos cadastrados:");
        for (Produto produto : produtos) {
            System.out.printf(
                    "ID: %d | Nome: %s | Preço: %s | Estoque: %d | Categoria: %s%n",
                    produto.getId(),
                    produto.getNome(),
                    OutputUtils.formatarMoeda(produto.getPreco()),
                    produto.getQuantidadeEstoque(),
                    produto.getCategoria().getNome()
            );
        }
    }

    private void cadastrarCliente() {
        String nome = InputUtils.lerTexto(scanner, "Nome do cliente: ");
        String cpf = InputUtils.lerTexto(scanner, "CPF: ");
        String telefone = InputUtils.lerTexto(scanner, "Telefone: ");
        String email = InputUtils.lerTexto(scanner, "E-mail: ");

        Cliente cliente = sistema.getClienteService().cadastrar(nome, cpf, telefone, email);
        System.out.println("Cliente cadastrado com sucesso. ID: " + cliente.getId());
    }

    private void listarClientes() {
        List<Cliente> clientes = sistema.getClienteService().listarTodos();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("Clientes cadastrados:");
        for (Cliente cliente : clientes) {
            System.out.printf(
                    "ID: %d | Nome: %s | CPF: %s | Telefone: %s | E-mail: %s%n",
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    OutputUtils.valorOuPadrao(cliente.getTelefone()),
                    OutputUtils.valorOuPadrao(cliente.getEmail())
            );
        }
    }

    private void realizarVenda() {
        long clienteId = InputUtils.lerLong(scanner, "ID do cliente: ");
        Cliente cliente = sistema.getClienteService().buscarPorId(clienteId);
        Venda venda = sistema.getVendaService().iniciarVenda(cliente);

        boolean adicionandoItens = true;
        while (adicionandoItens) {
            long produtoId = InputUtils.lerLong(scanner, "ID do produto: ");
            int quantidade = InputUtils.lerInteiro(scanner, "Quantidade: ");
            Produto produto = sistema.getProdutoService().buscarPorId(produtoId);
            sistema.getVendaService().adicionarItem(venda, produto, quantidade);

            String continuar = InputUtils.lerTexto(scanner, "Adicionar outro item? (s/n): ");
            adicionandoItens = continuar.equalsIgnoreCase("s");
        }

        Venda vendaFinalizada = sistema.getVendaService().finalizarVenda(venda);
        System.out.println("Venda finalizada com sucesso. ID: " + vendaFinalizada.getId());
        System.out.println("Total da venda: " + OutputUtils.formatarMoeda(vendaFinalizada.calcularTotal()));
    }

    private void listarVendas() {
        List<Venda> vendas = sistema.getVendaService().listarTodas();

        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda realizada.");
            return;
        }

        System.out.println("Vendas realizadas:");
        for (Venda venda : vendas) {
            System.out.printf(
                    "ID: %d | Cliente: %s | Itens: %d | Total: %s | Data/Hora: %s%n",
                    venda.getId(),
                    venda.getCliente().getNome(),
                    venda.getItens().size(),
                    OutputUtils.formatarMoeda(venda.calcularTotal()),
                    OutputUtils.formatarDataHora(venda.getDataHora())
            );
        }
    }
}
