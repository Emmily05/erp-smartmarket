package com.smartmarket.bean;

import com.smartmarket.model.Fornecedor;
import com.smartmarket.service.FornecedorService;
import com.smartmarket.util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class FornecedorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private FornecedorService fornecedorService = new FornecedorService();
    private Fornecedor fornecedor;
    private List<Fornecedor> fornecedores;

    @PostConstruct
    public void init() {
        fornecedor = new Fornecedor();
        carregarFornecedores();
    }

    public void carregarFornecedores() {
        fornecedores = fornecedorService.findAll();
    }

    public void novo() {
        fornecedor = new Fornecedor();
    }

    public void salvar() {
        try {
            fornecedorService.save(fornecedor);
            carregarFornecedores();
            FacesUtil.addInfoMessage("Sucesso", "Fornecedor salvo com sucesso!");
            fornecedor = new Fornecedor(); 
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro", "Erro ao salvar fornecedor: " + e.getMessage());
        }
    }

    public void excluir() {
        try {
            fornecedorService.delete(fornecedor.getId());
            carregarFornecedores();
            FacesUtil.addInfoMessage("Sucesso", "Fornecedor excluído com sucesso!");
            fornecedor = new Fornecedor();
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro", "Erro ao excluir fornecedor: " + e.getMessage());
        }
    }
    

    public void prepararEdicao(Fornecedor f) {
        this.fornecedor = f;
    }

    public void prepararExclusao(Fornecedor f) {
        this.fornecedor = f;
    }

    // Getters e Setters
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<Fornecedor> getFornecedores() {
        return fornecedores;
    }
}
