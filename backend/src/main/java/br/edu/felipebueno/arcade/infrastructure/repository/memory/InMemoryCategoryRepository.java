package br.edu.felipebueno.arcade.infrastructure.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public synchronized Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idGenerator.getAndIncrement());
        }

        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public synchronized Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized Optional<Category> findByNameIgnoreCase(String name) {
        if (name == null) {
            return Optional.empty();
        }

        String normalizedName = name.trim().toLowerCase(Locale.ROOT);
        return storage.values().stream()
                .filter(category -> category.getName().toLowerCase(Locale.ROOT).equals(normalizedName))
                .findFirst();
    }

    @Override
    public synchronized List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized void deleteById(Long id) {
        storage.remove(id);
    }
}
