package br.edu.felipebueno.arcade.api.controller;

import br.edu.felipebueno.arcade.api.dto.CategoryDto;
import br.edu.felipebueno.arcade.application.service.CategoryService;
import br.edu.felipebueno.arcade.domain.model.Category;
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
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto.Response> list() {
        return categoryService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryDto.Response find(@PathVariable Long id) {
        return toResponse(categoryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto.Response create(@Valid @RequestBody CategoryDto.CreateRequest request) {
        return toResponse(categoryService.create(request.name()));
    }

    @PutMapping("/{id}")
    public CategoryDto.Response update(@PathVariable Long id, @Valid @RequestBody CategoryDto.CreateRequest request) {
        return toResponse(categoryService.update(id, request.name()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    private CategoryDto.Response toResponse(Category category) {
        return new CategoryDto.Response(category.getId(), category.getName());
    }
}
