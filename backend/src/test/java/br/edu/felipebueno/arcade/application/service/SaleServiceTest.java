package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.model.Sale;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemoryCustomerRepository;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemoryProductRepository;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemorySaleRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaleServiceTest {

    @Test
    void shouldRegisterSaleAndReduceStock() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        SaleService saleService = new SaleService(
                new InMemorySaleRepository(),
                customerRepository,
                productRepository
        );

        Customer customer = customerRepository.save(new Customer(null, "John", "123.456.789-00", null, null));
        Product product = productRepository.save(new Product(
                null,
                "Arcade Controller",
                "USB controller",
                new BigDecimal("199.90"),
                5,
                new Category(1L, "Games")
        ));

        Sale sale = saleService.register(
                customer.getId(),
                List.of(new SaleService.RequestedItem(product.getId(), 2))
        );

        assertEquals(1L, sale.getId());
        assertEquals(new BigDecimal("399.80"), sale.calculateTotal());
        assertEquals(3, product.getStockQuantity());
    }

    @Test
    void shouldRejectSaleWithoutStockWithoutReducingOtherProducts() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        SaleService saleService = new SaleService(
                new InMemorySaleRepository(),
                customerRepository,
                productRepository
        );

        Customer customer = customerRepository.save(new Customer(null, "Lucas", "987.654.321-00", null, null));
        Category category = new Category(1L, "Games");
        Product controller = productRepository.save(new Product(
                null,
                "Arcade Controller",
                "USB controller",
                new BigDecimal("199.90"),
                5,
                category
        ));
        Product tokenPack = productRepository.save(new Product(
                null,
                "Arcade Tokens",
                "Token pack",
                new BigDecimal("10.00"),
                1,
                category
        ));

        assertThrows(BusinessException.class, () -> saleService.register(
                customer.getId(),
                List.of(
                        new SaleService.RequestedItem(controller.getId(), 2),
                        new SaleService.RequestedItem(tokenPack.getId(), 2)
                )
        ));

        assertEquals(5, controller.getStockQuantity());
        assertEquals(1, tokenPack.getStockQuantity());
    }
}
