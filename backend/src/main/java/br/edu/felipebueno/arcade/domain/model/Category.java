package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;

public class Category {
    private Long id;
    private String name;

    public Category(Long id, String name) {
        this.id = id;
        rename(name);
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Category name is required.");
        }
        this.name = name.trim();
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
}
