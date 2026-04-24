package br.edu.felipebueno.arcade.domain.repository;

import br.edu.felipebueno.arcade.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findAll();

    void deleteById(Long id);
}
