package br.edu.felipebueno.arcade.domain.model;

import java.util.Objects;

public class Categoria {
    private Long id;
    private String nome;

    public Categoria(Long id, String nome) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "O nome da categoria é obrigatório.").trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome da categoria é obrigatório.").trim();
    }
}
