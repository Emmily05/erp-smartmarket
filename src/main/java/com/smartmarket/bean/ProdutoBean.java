package com.smartmarket.bean;

import com.smartmarket.model.Fornecedor;
import com.smartmarket.model.Produto;
import com.smartmarket.service.FornecedorService;
import com.smartmarket.service.ProdutoService;
import com.smartmarket.util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProdutoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ProdutoService produtoService = new ProdutoService();
    private final FornecedorService fornecedorService = new FornecedorService();

    private Produto produto;
    private List<Produto> produtos;
    private List<Fornecedor> fornecedores;
    private String filtroAtivo = "ATIVOS"; 
    
    @PostConstruct
    public void init() {
        novo();
        carregarProdutos();
        carregarFornecedores();
    }

    public void carregarProdutos() {
        switch (filtroAtivo) {
            case "ATIVOS":
                produtos = produtoService.findAllAtivos();
                break;
            case "INATIVOS":
                produtos = produtoService.findAllInativos(); 
                break;
            case "TODOS":
            default:
                produtos = produtoService.findAll();
                break;
        }
    }

    public void carregarFornecedores() {
        fornecedores = fornecedorService.findAll();
    }

    public void novo() {
        this.produto = new Produto();
    }

    public void prepararEdicao(Produto p) {
        this.produto = produtoService.findById(p.getId());
    }

    public void prepararExclusao(Produto p) {
        this.produto = p;
    }

    public void salvar() {
        try {
            produtoService.save(produto);
            carregarProdutos();
            FacesUtil.addInfoMessage("Sucesso", "Produto salvo com sucesso!");
            novo();
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro", "Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluir() {
        if (produto == null || produto.getId() == null) {
            FacesUtil.addErrorMessage("Erro", "Nenhum produto selecionado.");
            return;
        }

        try {
            produto.setAtivo(false);
            produtoService.save(produto);
            carregarProdutos();
            FacesUtil.addInfoMessage("Sucesso", "Produto desativado com sucesso!");
            novo();
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro", "Erro ao desativar: " + e.getMessage());
        }
    }
   

    // GETTERS E SETTERS
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public List<Produto> getProdutos() { return produtos; }
    public List<Fornecedor> getFornecedores() { return fornecedores; }
    public String getFiltroAtivo() { return filtroAtivo;}
    public void setFiltroAtivo(String filtroAtivo) {this.filtroAtivo = filtroAtivo;}
    
}