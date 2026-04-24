package br.edu.felipebueno.arcade.infrastructure.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Sale;
import br.edu.felipebueno.arcade.domain.repository.SaleRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemorySaleRepository implements SaleRepository {
    private final Map<Long, Sale> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public synchronized Sale save(Sale sale) {
        if (sale.getId() == null) {
            sale.setId(idGenerator.getAndIncrement());
        }

        storage.put(sale.getId(), sale);
        return sale;
    }

    @Override
    public synchronized Optional<Sale> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized List<Sale> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized boolean existsByCustomerId(Long customerId) {
        if (customerId == null) {
            return false;
        }

        return storage.values().stream()
                .anyMatch(sale -> customerId.equals(sale.getCustomer().getId()));
    }

    @Override
    public synchronized boolean existsByProductId(Long productId) {
        if (productId == null) {
            return false;
        }

        return storage.values().stream()
                .flatMap(sale -> sale.getItems().stream())
                .anyMatch(item -> productId.equals(item.getProduct().getId()));
    }
}
