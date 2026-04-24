package br.edu.felipebueno.arcade.api.controller;

import br.edu.felipebueno.arcade.api.dto.CustomerDto;
import br.edu.felipebueno.arcade.application.service.CustomerService;
import br.edu.felipebueno.arcade.domain.model.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto.Response> list() {
        return customerService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CustomerDto.Response find(@PathVariable Long id) {
        return toResponse(customerService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto.Response create(@Valid @RequestBody CustomerDto.CreateRequest request) {
        Customer customer = customerService.create(
                request.name(),
                request.cpf(),
                request.phone(),
                request.email()
        );

        return toResponse(customer);
    }

    @PutMapping("/{id}")
    public CustomerDto.Response update(@PathVariable Long id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
        Customer customer = customerService.update(id, request.name(), request.phone(), request.email());
        return toResponse(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }

    private CustomerDto.Response toResponse(Customer customer) {
        return new CustomerDto.Response(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getEmail()
        );
    }
}
