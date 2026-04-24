package br.edu.felipebueno.arcade.domain.repository;

import br.edu.felipebueno.arcade.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByCpf(String cpf);

    List<Customer> findAll();

    void deleteById(Long id);
}
