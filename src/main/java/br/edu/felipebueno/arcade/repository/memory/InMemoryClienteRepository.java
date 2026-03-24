package br.edu.felipebueno.arcade.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Cliente;
import br.edu.felipebueno.arcade.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryClienteRepository implements ClienteRepository {
    private final Map<Long, Cliente> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(idGenerator.getAndIncrement());
        }

        storage.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return storage.values().stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst();
    }

    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
