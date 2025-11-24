package com.smartmarket.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Perfil {
        ADMIN, GERENTE, CAIXA, CLIENTE
    }

    private Long id;
    private String nome;
    private String email;
    private String senha; 
    private Perfil perfil;
    private boolean ativo;
    private LocalDateTime criadoEm;

    public Usuario() {
        this.ativo = true;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", perfil=" + perfil + "]";
    }
}
