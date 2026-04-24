package br.edu.felipebueno.arcade.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public final class ProductDto {

    private ProductDto() {
    }

    public record CreateRequest(
            @NotBlank(message = "Enter the product name.")
            @Size(max = 150, message = "Product name must have at most 150 characters.")
            String name,

            @Size(max = 255, message = "Description must have at most 255 characters.")
            String description,

            @NotNull(message = "Enter the product price.")
            @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
            BigDecimal price,

            @NotNull(message = "Enter the stock quantity.")
            @PositiveOrZero(message = "Stock quantity cannot be negative.")
            Integer stockQuantity,

            @NotNull(message = "Enter the product category.")
            @Positive(message = "Product category is invalid.")
            Long categoryId
    ) {
    }

    public record UpdateRequest(
            @NotBlank(message = "Enter the product name.")
            @Size(max = 150, message = "Product name must have at most 150 characters.")
            String name,

            @Size(max = 255, message = "Description must have at most 255 characters.")
            String description,

            @NotNull(message = "Enter the product price.")
            @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
            BigDecimal price,

            @NotNull(message = "Enter the product category.")
            @Positive(message = "Product category is invalid.")
            Long categoryId
    ) {
    }

    public record StockChangeRequest(
            @NotNull(message = "Enter the quantity.")
            @Positive(message = "Quantity must be greater than zero.")
            Integer quantity
    ) {
    }

    public record Response(
            Long id,
            String name,
            String description,
            BigDecimal price,
            int stockQuantity,
            CategoryDto.Response category
    ) {
    }
}
