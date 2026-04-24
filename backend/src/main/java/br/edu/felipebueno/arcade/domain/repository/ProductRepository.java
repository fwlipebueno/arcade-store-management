package br.edu.felipebueno.arcade.domain.repository;

import br.edu.felipebueno.arcade.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    boolean existsByCategoryId(Long categoryId);

    void deleteById(Long id);
}
