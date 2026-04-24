package br.edu.felipebueno.arcade.domain.repository;

import br.edu.felipebueno.arcade.domain.model.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {

    Sale save(Sale sale);

    Optional<Sale> findById(Long id);

    List<Sale> findAll();

    boolean existsByCustomerId(Long customerId);

    boolean existsByProductId(Long productId);
}
