package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.model.SaleItem;
import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.model.Sale;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemoryCustomerRepository;
import br.edu.felipebueno.arcade.infrastructure.repository.memory.InMemorySaleRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerServiceTest {

    @Test
    void shouldCreateCustomer() {
        CustomerService customerService = new CustomerService(
                new InMemoryCustomerRepository(),
                new InMemorySaleRepository()
        );

        Customer customer = customerService.create(
                "Felipe Bueno",
                "000.000.000-00",
                "(43) 99999-0000",
                "felipe@example.com"
        );

        assertEquals(1L, customer.getId());
        assertEquals("Felipe Bueno", customer.getName());
    }

    @Test
    void shouldRejectDuplicateCpf() {
        CustomerService customerService = new CustomerService(
                new InMemoryCustomerRepository(),
                new InMemorySaleRepository()
        );

        customerService.create(
                "Felipe Bueno",
                "000.000.000-00",
                "(43) 99999-0000",
                "felipe@example.com"
        );

        assertThrows(BusinessException.class, () -> customerService.create(
                "Another Customer",
                "000.000.000-00",
                "(43) 98888-0000",
                "another@example.com"
        ));
    }

    @Test
    void shouldRejectDeletingCustomerLinkedToSale() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemorySaleRepository saleRepository = new InMemorySaleRepository();
        CustomerService customerService = new CustomerService(customerRepository, saleRepository);

        Customer customer = customerRepository.save(new Customer(null, "Felipe Bueno", "000.000.000-00", null, null));
        Product product = new Product(
                1L,
                "Arcade Controller",
                "USB controller",
                new BigDecimal("199.90"),
                3,
                new Category(1L, "Games")
        );
        Sale sale = new Sale(null, customer);
        sale.addItem(new SaleItem(product, 1, product.getPrice()));
        saleRepository.save(sale);

        assertThrows(BusinessException.class, () -> customerService.delete(customer.getId()));
    }
}
