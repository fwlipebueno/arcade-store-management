package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.exception.ResourceNotFoundException;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.repository.CategoryRepository;
import br.edu.felipebueno.arcade.domain.repository.ProductRepository;
import br.edu.felipebueno.arcade.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SaleRepository saleRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
    }

    public Product create(String name, String description, BigDecimal price, int stockQuantity, Long categoryId) {
        Category category = findCategory(categoryId);
        Product product = new Product(null, name, description, price, stockQuantity, category);
        return productRepository.save(product);
    }

    public Product update(Long id, String name, String description, BigDecimal price, Long categoryId) {
        Product product = findById(id);
        Category category = findCategory(categoryId);

        product.updateDetails(name, description, price, category);
        return productRepository.save(product);
    }

    public Product addStock(Long id, int quantity) {
        Product product = findById(id);
        product.addStock(quantity);
        return productRepository.save(product);
    }

    public Product removeStock(Long id, int quantity) {
        Product product = findById(id);
        product.removeStock(quantity);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        findById(id);

        if (saleRepository.existsByProductId(id)) {
            throw new BusinessException("Cannot delete a product linked to sales.");
        }

        productRepository.deleteById(id);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }
}
