package com.smartmarket.bean;

import com.smartmarket.model.Produto;
import com.smartmarket.service.ProdutoService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoService produtoService = new ProdutoService();
    private List<Produto> produtosEstoqueBaixo;
    private int quantidadeCriticos;

    @PostConstruct
    public void init() {
        produtosEstoqueBaixo = produtoService.findEstoqueCritico();
        quantidadeCriticos = (int) produtosEstoqueBaixo.stream().filter(Produto::isEstoqueCritico).count();
    }

    public List<Produto> getProdutosEstoqueBaixo() {
        return produtosEstoqueBaixo;
    }

    public boolean isTemEstoqueCritico() {
        return quantidadeCriticos > 0;
    }

    public int getQuantidadeCriticos() {
        return quantidadeCriticos;
    }
}
