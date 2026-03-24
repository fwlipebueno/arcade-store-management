package br.edu.felipebueno.arcade.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Produto;
import br.edu.felipebueno.arcade.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProdutoRepository implements ProdutoRepository {
    private final Map<Long, Produto> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(idGenerator.getAndIncrement());
        }

        storage.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Produto> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
