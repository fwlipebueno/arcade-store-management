package br.edu.felipebueno.arcade.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class CategoryDto {

    private CategoryDto() {
    }

    public record CreateRequest(
            @NotBlank(message = "Enter the category name.")
            @Size(max = 100, message = "Category name must have at most 100 characters.")
            String name
    ) {
    }

    public record Response(Long id, String name) {
    }
}
