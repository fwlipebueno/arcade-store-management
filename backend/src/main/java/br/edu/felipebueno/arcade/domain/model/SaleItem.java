package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public class SaleItem {
    private final Product product;
    private final int quantity;
    private final BigDecimal unitPrice;

    public SaleItem(Product product, int quantity, BigDecimal unitPrice) {
        this.product = Objects.requireNonNull(product, "Sale item product is required.");

        if (quantity <= 0) {
            throw new BusinessException("Sale item quantity must be greater than zero.");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Unit price must be greater than zero.");
        }

        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
