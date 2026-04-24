package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;

public class Customer {
    private Long id;
    private String name;
    private String cpf;
    private String phone;
    private String email;

    public Customer(Long id, String name, String cpf, String phone, String email) {
        this.id = id;
        this.name = requiredText(name, "Customer name is required.");
        this.cpf = requiredText(cpf, "Customer CPF is required.");
        this.phone = optionalText(phone);
        this.email = optionalText(email);
    }

    public void updateDetails(String name, String phone, String email) {
        this.name = requiredText(name, "Customer name is required.");
        this.phone = optionalText(phone);
        this.email = optionalText(email);
    }

    private String requiredText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(message);
        }
        return value.trim();
    }

    private String optionalText(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
