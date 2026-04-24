package br.edu.felipebueno.arcade.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class CustomerDto {

    private CustomerDto() {
    }

    public record CreateRequest(
            @NotBlank(message = "Enter the customer name.")
            @Size(max = 150, message = "Customer name must have at most 150 characters.")
            String name,

            @NotBlank(message = "Enter the customer CPF.")
            @Size(max = 14, message = "CPF must have at most 14 characters.")
            String cpf,

            @Size(max = 20, message = "Phone must have at most 20 characters.")
            String phone,

            @Email(message = "Enter a valid email address.")
            @Size(max = 150, message = "Email must have at most 150 characters.")
            String email
    ) {
    }

    public record UpdateRequest(
            @NotBlank(message = "Enter the customer name.")
            @Size(max = 150, message = "Customer name must have at most 150 characters.")
            String name,

            @Size(max = 20, message = "Phone must have at most 20 characters.")
            String phone,

            @Email(message = "Enter a valid email address.")
            @Size(max = 150, message = "Email must have at most 150 characters.")
            String email
    ) {
    }

    public record Response(Long id, String name, String cpf, String phone, String email) {
    }
}
