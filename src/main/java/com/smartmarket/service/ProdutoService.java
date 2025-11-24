package com.smartmarket.service;

import com.smartmarket.dao.ProdutoDAO;
import com.smartmarket.model.Produto;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void save(Produto produto) {
        if (produto.getId() == null) {
            produtoDAO.insert(produto);
        } else {
            produtoDAO.update(produto);
        }
    }

    public void delete(Long id) {
        produtoDAO.delete(id);
    }

    public void darBaixaEstoque(Produto produto, int quantidade) {
        produto.setEstoque(produto.getEstoque().subtract(new BigDecimal(quantidade)));
        produtoDAO.update(produto);
    }
	
	    public List<Produto> findAllInativos() {
	        return produtoDAO.findInativos();
	    }
	
	    public List<Produto> findAllAtivos() {
	        return produtoDAO.findAtivos(); 
	    }
    

    public List<Produto> findEstoqueCritico() {
        return produtoDAO.findEstoqueCritico(); 
    }

    public List<Produto> findAll() {
        return produtoDAO.findAll();
    }
    
    public Produto findById(Long id) {
        return produtoDAO.findById(id);
    }
}