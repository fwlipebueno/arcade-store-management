package br.edu.felipebueno.arcade.domain.model;

import java.util.Objects;

public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    public Cliente(Long id, String nome, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "O nome do cliente é obrigatório.").trim();
        this.cpf = Objects.requireNonNull(cpf, "O CPF do cliente é obrigatório.").trim();
        this.telefone = telefone == null ? null : telefone.trim();
        this.email = email == null ? null : email.trim();
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
        this.nome = Objects.requireNonNull(nome, "O nome do cliente é obrigatório.").trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = Objects.requireNonNull(cpf, "O CPF do cliente é obrigatório.").trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone == null ? null : telefone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}
