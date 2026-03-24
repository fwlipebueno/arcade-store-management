package br.edu.felipebueno.arcade.service;

import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.exception.BusinessException;
import br.edu.felipebueno.arcade.repository.ClienteRepository;

import java.util.List;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(String nome, String cpf, String telefone, String email) {
        clienteRepository.findByCpf(cpf).ifPresent(cliente -> {
            throw new BusinessException("Já existe um cliente cadastrado com esse CPF.");
        });

        Cliente cliente = new Cliente(null, nome, cpf, telefone, email);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long clienteId, String nome, String telefone, String email) {
        Cliente cliente = buscarPorId(clienteId);
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        return clienteRepository.save(cliente);
    }

    public void remover(Long clienteId) {
        buscarPorId(clienteId);
        clienteRepository.deleteById(clienteId);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}
