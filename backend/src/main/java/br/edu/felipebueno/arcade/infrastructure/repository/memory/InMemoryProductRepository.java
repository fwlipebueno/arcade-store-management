package br.edu.felipebueno.arcade.infrastructure.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public synchronized Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }

        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public synchronized Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized boolean existsByCategoryId(Long categoryId) {
        return storage.values().stream()
                .anyMatch(product -> product.getCategory().getId().equals(categoryId));
    }

    @Override
    public synchronized void deleteById(Long id) {
        storage.remove(id);
    }
}
