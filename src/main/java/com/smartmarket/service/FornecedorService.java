package com.smartmarket.service;

import com.smartmarket.dao.FornecedorDAO;
import com.smartmarket.model.Fornecedor;

import java.util.List;

public class FornecedorService {

    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public void save(Fornecedor fornecedor) {
        if (fornecedor.getId() == null) {
            fornecedorDAO.insert(fornecedor);
        } else {
            fornecedorDAO.update(fornecedor);
        }
    }

    public List<Fornecedor> findAll() {
        return fornecedorDAO.findAll();
    }
    
    public void delete(Long id) {
        fornecedorDAO.delete(id);
    }
}
