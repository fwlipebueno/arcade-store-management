package br.edu.felipebueno.arcade.repository;

import br.edu.felipebueno.arcade.domain.model.Venda;

import java.util.List;
import java.util.Optional;

public interface VendaRepository {
    Venda save(Venda venda);

    Optional<Venda> findById(Long id);

    List<Venda> findAll();
}
