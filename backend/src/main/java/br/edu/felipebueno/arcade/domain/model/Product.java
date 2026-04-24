package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private Category category;

    public Product(Long id, String name, String description, BigDecimal price, int stockQuantity, Category category) {
        this.id = id;
        updateDetails(name, description, price, category);
        defineInitialStockQuantity(stockQuantity);
    }

    public void updateDetails(String name, String description, BigDecimal price, Category category) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Product name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be greater than zero.");
        }

        this.name = name.trim();
        this.description = description == null || description.isBlank() ? null : description.trim();
        this.price = price;
        this.category = Objects.requireNonNull(category, "Product category is required.");
    }

    public void addStock(int quantity) {
        validateQuantity(quantity);
        stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        validateQuantity(quantity);

        if (!hasStock(quantity)) {
            throw new BusinessException("Insufficient stock for product " + name + ".");
        }

        stockQuantity -= quantity;
    }

    public boolean hasStock(int quantity) {
        return quantity > 0 && quantity <= stockQuantity;
    }

    private void defineInitialStockQuantity(int quantity) {
        if (quantity < 0) {
            throw new BusinessException("Initial stock quantity cannot be negative.");
        }
        stockQuantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero.");
        }
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

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public Category getCategory() {
        return category;
    }
}
