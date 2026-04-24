package br.edu.felipebueno.arcade.api.controller;

import br.edu.felipebueno.arcade.api.dto.SaleDto;
import br.edu.felipebueno.arcade.application.service.SaleService;
import br.edu.felipebueno.arcade.domain.model.Customer;
import br.edu.felipebueno.arcade.domain.model.SaleItem;
import br.edu.felipebueno.arcade.domain.model.Sale;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleDto.Response> list() {
        return saleService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SaleDto.Response find(@PathVariable Long id) {
        return toResponse(saleService.findById(id));
    }

    @GetMapping("/report")
    public SaleDto.Report report() {
        SaleService.SalesSummary summary = saleService.generateSummary();
        return new SaleDto.Report(
                summary.salesCount(),
                summary.itemsSold(),
                summary.totalRevenue()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDto.Response register(@Valid @RequestBody SaleDto.CreateRequest request) {
        List<SaleService.RequestedItem> items = request.items().stream()
                .map(item -> new SaleService.RequestedItem(item.productId(), item.quantity()))
                .toList();

        return toResponse(saleService.register(request.customerId(), items));
    }

    private SaleDto.Response toResponse(Sale sale) {
        Customer customer = sale.getCustomer();
        SaleDto.CustomerSummary customerResponse = new SaleDto.CustomerSummary(
                customer.getId(),
                customer.getName(),
                customer.getCpf()
        );

        List<SaleDto.ItemResponse> items = sale.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new SaleDto.Response(
                sale.getId(),
                customerResponse,
                sale.getSaleDateTime(),
                items,
                sale.calculateTotal()
        );
    }

    private SaleDto.ItemResponse toItemResponse(SaleItem item) {
        return new SaleDto.ItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.calculateSubtotal()
        );
    }
}
