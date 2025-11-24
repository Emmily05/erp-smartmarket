package com.smartmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String nome;
    private BigDecimal preco;
    private BigDecimal estoque;
    private BigDecimal estoqueMinimo;
    private Long fornecedorId;
    private boolean ativo;
    
    private Fornecedor fornecedor; 

    public Produto() {
        this.ativo = true;
        this.estoque = BigDecimal.ZERO;
        this.estoqueMinimo = new BigDecimal("5.0");
        this.preco = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getEstoque() {
        return estoque;
    }

    public void setEstoque(BigDecimal estoque) {
        this.estoque = estoque;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + ", preco=" + preco + ", estoque=" + estoque + "]";
    }
    
    public boolean isEstoqueCritico() {
        return estoque.compareTo(estoqueMinimo) < 0;
    }
}
