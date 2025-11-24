package com.smartmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemVenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long vendaId;
    private Long produtoId;
    private Produto produto;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;

    public ItemVenda() {
        this.quantidade = BigDecimal.ONE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.produtoId = produto.getId();
        this.precoUnitario = produto.getPreco();
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(quantidade);
    }
}
