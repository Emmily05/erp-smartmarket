package com.smartmarket.service;

import com.smartmarket.dao.VendaDAO;
import com.smartmarket.model.Venda;

public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();

    public void save(Venda venda) {
        vendaDAO.insert(venda);
    }
    
    public Venda findUltimaVenda() {
        return vendaDAO.findUltimaVenda();
    }
}
