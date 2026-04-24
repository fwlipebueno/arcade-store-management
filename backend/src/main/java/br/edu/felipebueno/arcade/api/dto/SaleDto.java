package br.edu.felipebueno.arcade.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class SaleDto {

    private SaleDto() {
    }

    public record CreateRequest(
            @NotNull(message = "Enter the sale customer.")
            @Positive(message = "Sale customer is invalid.")
            Long customerId,

            @NotEmpty(message = "Enter at least one sale item.")
            List<@Valid ItemRequest> items
    ) {
    }

    public record ItemRequest(
            @NotNull(message = "Enter the sale item product.")
            @Positive(message = "Sale item product is invalid.")
            Long productId,

            @Positive(message = "Sale item quantity must be greater than zero.")
            int quantity
    ) {
    }

    public record CustomerSummary(Long id, String name, String cpf) {
    }

    public record ItemResponse(
            Long productId,
            String productName,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
    }

    public record Response(
            Long id,
            CustomerSummary customer,
            LocalDateTime saleDateTime,
            List<ItemResponse> items,
            BigDecimal total
    ) {
    }

    public record Report(long salesCount, int itemsSold, BigDecimal totalRevenue) {
    }
}
