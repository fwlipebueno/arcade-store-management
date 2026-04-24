package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.exception.ResourceNotFoundException;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.repository.CustomerRepository;
import br.edu.felipebueno.arcade.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;

    public CustomerService(CustomerRepository customerRepository, SaleRepository saleRepository) {
        this.customerRepository = customerRepository;
        this.saleRepository = saleRepository;
    }

    public Customer create(String name, String cpf, String phone, String email) {
        Customer customer = new Customer(null, name, cpf, phone, email);

        customerRepository.findByCpf(customer.getCpf()).ifPresent(existingCustomer -> {
            throw new BusinessException("A customer with this CPF already exists.");
        });

        return customerRepository.save(customer);
    }

    public Customer update(Long id, String name, String phone, String email) {
        Customer customer = findById(id);
        customer.updateDetails(name, phone, email);
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        findById(id);

        if (saleRepository.existsByCustomerId(id)) {
            throw new BusinessException("Cannot delete a customer linked to sales.");
        }

        customerRepository.deleteById(id);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
