package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.model.SaleItem;
import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.model.Sale;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemoryCategoryRepository;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemoryProductRepository;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemorySaleRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {

    @Test
    void shouldCreateProductWithExistingCategory() {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(
                productRepository,
                categoryRepository,
                new InMemorySaleRepository()
        );

        Category category = categoryRepository.save(new Category(null, "Accessories"));

        Product product = productService.create(
                "Arcade Controller",
                "USB controller for games",
                new BigDecimal("199.90"),
                8,
                category.getId()
        );

        assertEquals(1L, product.getId());
        assertEquals("Arcade Controller", product.getName());
        assertEquals(8, product.getStockQuantity());
        assertEquals("Accessories", product.getCategory().getName());
    }

    @Test
    void shouldRejectInvalidPrice() {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        ProductService productService = new ProductService(
                new InMemoryProductRepository(),
                categoryRepository,
                new InMemorySaleRepository()
        );

        Category category = categoryRepository.save(new Category(null, "Accessories"));

        assertThrows(BusinessException.class, () -> productService.create(
                "Arcade Controller",
                "USB controller for games",
                BigDecimal.ZERO,
                8,
                category.getId()
        ));
    }

    @Test
    void shouldRejectDeletingProductLinkedToSale() {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemorySaleRepository saleRepository = new InMemorySaleRepository();
        ProductService productService = new ProductService(
                productRepository,
                categoryRepository,
                saleRepository
        );

        Category category = categoryRepository.save(new Category(null, "Games"));
        Product product = productRepository.save(new Product(
                null,
                "Arcade Controller",
                "USB controller",
                new BigDecimal("199.90"),
                3,
                category
        ));
        Sale sale = new Sale(null, new Customer(1L, "Felipe Bueno", "000.000.000-00", null, null));
        sale.addItem(new SaleItem(product, 1, product.getPrice()));
        saleRepository.save(sale);

        assertThrows(BusinessException.class, () -> productService.delete(product.getId()));
    }
}
