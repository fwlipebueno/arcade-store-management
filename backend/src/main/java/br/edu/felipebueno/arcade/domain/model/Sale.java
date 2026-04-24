package br.edu.felipebueno.arcade.domain.model;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sale {
    private Long id;
    private final Customer customer;
    private final LocalDateTime saleDateTime;
    private final List<SaleItem> items = new ArrayList<>();

    public Sale(Long id, Customer customer) {
        this(id, customer, LocalDateTime.now());
    }

    public Sale(Long id, Customer customer, LocalDateTime saleDateTime) {
        this.id = id;
        this.customer = Objects.requireNonNull(customer, "Sale customer is required.");
        this.saleDateTime = Objects.requireNonNull(saleDateTime, "Sale date and time are required.");
    }

    public void addItem(SaleItem item) {
        if (item == null) {
            throw new BusinessException("Sale item is required.");
        }
        items.add(item);
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(SaleItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validate() {
        if (items.isEmpty()) {
            throw new BusinessException("Sale must have at least one item.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public List<SaleItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
