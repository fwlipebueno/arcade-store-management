package br.edu.felipebueno.arcade.repository.memory;

import br.edu.felipebueno.arcade.domain.model.Venda;
import br.edu.felipebueno.arcade.repository.VendaRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryVendaRepository implements VendaRepository {
    private final Map<Long, Venda> storage = new LinkedHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Venda save(Venda venda) {
        if (venda.getId() == null) {
            venda.setId(idGenerator.getAndIncrement());
        }

        storage.put(venda.getId(), venda);
        return venda;
    }

    @Override
    public Optional<Venda> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Venda> findAll() {
        return new ArrayList<>(storage.values());
    }
}
