package br.edu.felipebueno.arcade.api.controller;

import br.edu.felipebueno.arcade.api.dto.CategoryDto;
import br.edu.felipebueno.arcade.api.dto.ProductDto;
import br.edu.felipebueno.arcade.application.service.ProductService;
import br.edu.felipebueno.arcade.domain.model.Category;
import br.edu.felipebueno.arcade.domain.model.Product;
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
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto.Response> list() {
        return productService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto.Response find(@PathVariable Long id) {
        return toResponse(productService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto.Response create(@Valid @RequestBody ProductDto.CreateRequest request) {
        Product product = productService.create(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                request.categoryId()
        );

        return toResponse(product);
    }

    @PutMapping("/{id}")
    public ProductDto.Response update(@PathVariable Long id, @Valid @RequestBody ProductDto.UpdateRequest request) {
        Product product = productService.update(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.categoryId()
        );

        return toResponse(product);
    }

    @PostMapping("/{id}/stock/in")
    public ProductDto.Response addStock(@PathVariable Long id, @Valid @RequestBody ProductDto.StockChangeRequest request) {
        return toResponse(productService.addStock(id, request.quantity()));
    }

    @PostMapping("/{id}/stock/out")
    public ProductDto.Response removeStock(@PathVariable Long id, @Valid @RequestBody ProductDto.StockChangeRequest request) {
        return toResponse(productService.removeStock(id, request.quantity()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    private ProductDto.Response toResponse(Product product) {
        Category category = product.getCategory();
        CategoryDto.Response categoryResponse = new CategoryDto.Response(category.getId(), category.getName());

        return new ProductDto.Response(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                categoryResponse
        );
    }
}
