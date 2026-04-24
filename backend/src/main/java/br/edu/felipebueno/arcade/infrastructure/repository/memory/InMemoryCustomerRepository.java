package br.edu.felipebueno.arcade.infrastructure.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<Long, Customer> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public synchronized Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(idGenerator.getAndIncrement());
        }

        storage.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public synchronized Optional<Customer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized Optional<Customer> findByCpf(String cpf) {
        if (cpf == null) {
            return Optional.empty();
        }

        return storage.values().stream()
                .filter(customer -> customer.getCpf().equals(cpf.trim()))
                .findFirst();
    }

    @Override
    public synchronized List<Customer> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized void deleteById(Long id) {
        storage.remove(id);
    }
}
