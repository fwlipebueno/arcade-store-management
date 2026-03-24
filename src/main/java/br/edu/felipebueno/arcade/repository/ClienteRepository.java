package br.edu.felipebueno.arcade.repository;

import br.edu.felipebueno.arcade.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByCpf(String cpf);

    List<Cliente> findAll();

    void deleteById(Long id);
}
