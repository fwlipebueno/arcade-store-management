package br.edu.felipebueno.arcade.application.service;

import br.edu.felipebueno.arcade.domain.exception.BusinessException;
import br.edu.felipebueno.arcade.domain.exception.ResourceNotFoundException;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.model.Inventory;
import br.edu.felipebueno.arcade.domain.model.SaleItem;
import br.edu.felipebueno.arcade.domain.model.Product;
import br.edu.felipebueno.arcade.domain.model.Sale;
import br.edu.felipebueno.arcade.domain.repository.CustomerRepository;
import br.edu.felipebueno.arcade.domain.repository.ProductRepository;
import br.edu.felipebueno.arcade.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final Inventory inventory = new Inventory();

    public SaleService(
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository
    ) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public synchronized Sale register(Long customerId, List<RequestedItem> requestedItems) {
        Customer customer = findCustomer(customerId);
        Map<Long, Integer> quantitiesByProduct = groupItems(requestedItems);
        Sale sale = new Sale(null, customer);
        Map<Product, Integer> validatedProducts = new LinkedHashMap<>();

        for (Map.Entry<Long, Integer> item : quantitiesByProduct.entrySet()) {
            Product product = findProduct(item.getKey());
            int quantity = item.getValue();

            if (!inventory.hasAvailableStock(product, quantity)) {
                throw new BusinessException("Insufficient stock for product " + product.getName() + ".");
            }

            validatedProducts.put(product, quantity);
        }

        for (Map.Entry<Product, Integer> item : validatedProducts.entrySet()) {
            Product product = item.getKey();
            int quantity = item.getValue();

            inventory.reserve(product, quantity);
            productRepository.save(product);
            sale.addItem(new SaleItem(product, quantity, product.getPrice()));
        }

        sale.validate();
        return saleRepository.save(sale);
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found."));
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public SalesSummary generateSummary() {
        List<Sale> sales = saleRepository.findAll();
        BigDecimal totalRevenue = sales.stream()
                .map(Sale::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int itemsSold = sales.stream()
                .flatMap(sale -> sale.getItems().stream())
                .mapToInt(SaleItem::getQuantity)
                .sum();

        return new SalesSummary(sales.size(), itemsSold, totalRevenue);
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    private Map<Long, Integer> groupItems(List<RequestedItem> requestedItems) {
        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new BusinessException("Sale must have at least one item.");
        }

        Map<Long, Integer> quantitiesByProduct = new LinkedHashMap<>();

        for (RequestedItem item : requestedItems) {
            if (item == null || item.productId() == null) {
                throw new BusinessException("Sale item product is required.");
            }
            if (item.quantity() <= 0) {
                throw new BusinessException("Sale item quantity must be greater than zero.");
            }

            quantitiesByProduct.merge(item.productId(), item.quantity(), Integer::sum);
        }

        return quantitiesByProduct;
    }

    public record RequestedItem(Long productId, int quantity) {
    }

    public record SalesSummary(long salesCount, int itemsSold, BigDecimal totalRevenue) {
    }
}
