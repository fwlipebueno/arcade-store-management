package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.exception.ResourceNotFoundException;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.repository.CategoryRepository;
import br.edu.felipebueno.arcade.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category create(String name) {
        Category category = new Category(null, name);
        ensureNameAvailable(category.getName(), null);
        return categoryRepository.save(category);
    }

    public Category update(Long id, String name) {
        Category category = findById(id);
        Category updatedCategory = new Category(id, name);

        ensureNameAvailable(updatedCategory.getName(), id);
        category.rename(updatedCategory.getName());

        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        findById(id);

        if (productRepository.existsByCategoryId(id)) {
            throw new BusinessException("Cannot delete a category linked to products.");
        }

        categoryRepository.deleteById(id);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void ensureNameAvailable(String name, Long currentId) {
        categoryRepository.findByNameIgnoreCase(name).ifPresent(category -> {
            if (!category.getId().equals(currentId)) {
                throw new BusinessException("A category with this name already exists.");
            }
        });
    }
}
