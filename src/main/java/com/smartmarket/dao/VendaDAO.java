package com.smartmarket.dao;

import com.smartmarket.model.FormaPagamento;
import com.smartmarket.model.ItemVenda;
import com.smartmarket.model.Venda;
import com.smartmarket.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO extends GenericDAO {

    private static final String INSERT_VENDA = "INSERT INTO venda (total, forma_pagamento, usuario_id) VALUES (?, ?, ?)";
    private static final String INSERT_ITEM_VENDA = "INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ULTIMA_VENDA = "SELECT * FROM venda ORDER BY id DESC LIMIT 1";
    private static final String SELECT_ITENS_VENDA = "SELECT * FROM item_venda WHERE venda_id = ?";

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void insert(Venda venda) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlVenda = """
                INSERT INTO venda 
                (data_hora, total, forma_pagamento, usuario_id, usuario_nome, valor_pago, troco) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement psVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                psVenda.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));           // DATA_HORA (OBRIGATÓRIO)
                psVenda.setBigDecimal(2, venda.getTotal());
                psVenda.setString(3, venda.getFormaPagamento().name());
                psVenda.setLong(4, venda.getUsuarioId());
                psVenda.setString(5, venda.getUsuario().getNome());                        // USUARIO_NOME (OBRIGATÓRIO)
                psVenda.setBigDecimal(6, venda.getValorPago() != null ? venda.getValorPago() : venda.getTotal());
                psVenda.setBigDecimal(7, venda.getTroco() != null ? venda.getTroco() : BigDecimal.ZERO);

                psVenda.executeUpdate();

                try (ResultSet rs = psVenda.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setId(rs.getLong(1));
                    }
                }
            }

            String sqlItem = """
                INSERT INTO venda_item 
                (venda_id, produto_id, produto_nome, quantidade, preco_unitario, subtotal) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                for (ItemVenda item : venda.getItens()) {
                    psItem.setLong(1, venda.getId());
                    psItem.setLong(2, item.getProduto().getId());
                    psItem.setString(3, item.getProduto().getNome());           
                    psItem.setInt(4, item.getQuantidade().intValue());
                    psItem.setBigDecimal(5, item.getProduto().getPreco());
                    psItem.setBigDecimal(6, item.getSubtotal());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { }
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar venda", e);
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
    
    public Venda findUltimaVenda() {
        Venda venda = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ULTIMA_VENDA);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                venda = mapResultSetToVenda(rs);
                venda.setItens(findItensVenda(venda.getId()));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar última venda: " + e.getMessage());
        }
        return venda;
    }
    
    private List<ItemVenda> findItensVenda(Long vendaId) {
        List<ItemVenda> itens = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ITENS_VENDA)) {
            
            ps.setLong(1, vendaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItemVenda item = new ItemVenda();
                    item.setId(rs.getLong("id"));
                    item.setVendaId(rs.getLong("venda_id"));
                    item.setProdutoId(rs.getLong("produto_id"));
                    item.setQuantidade(rs.getBigDecimal("quantidade"));
                    item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                    
                    
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens da venda: " + e.getMessage());
        }
        return itens;
    }

    private Venda mapResultSetToVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setDataHora(getLocalDateTime(rs, "data_hora"));
        venda.setTotal(rs.getBigDecimal("total"));
        String formaStr = rs.getString("forma_pagamento");
        venda.setFormaPagamento(formaStr != null ? FormaPagamento.valueOf(formaStr) : null);
        venda.setUsuarioId(rs.getLong("usuario_id"));
        

        venda.setUsuario(usuarioDAO.findById(venda.getUsuarioId()));
        
        
        return venda;
    }
}