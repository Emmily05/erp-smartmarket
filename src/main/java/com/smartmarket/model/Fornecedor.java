package com.smartmarket.model;

import java.io.Serializable;

public class Fornecedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String telefone;
    private String whatsapp;

    public Fornecedor() {
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
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    @Override
    public String toString() {
        return "Fornecedor [id=" + id + ", nome=" + nome + "]";
    }
}
