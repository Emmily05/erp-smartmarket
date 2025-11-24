package com.smartmarket.dao;

import com.smartmarket.model.Fornecedor;
import com.smartmarket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO extends GenericDAO {

    private static final String INSERT_FORNECEDOR = "INSERT INTO fornecedor (nome, telefone, whatsapp) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_FORNECEDORES = "SELECT * FROM fornecedor";
    private static final String SELECT_FORNECEDOR_BY_ID = "SELECT * FROM fornecedor WHERE id = ?";
    private static final String UPDATE_FORNECEDOR = "UPDATE fornecedor SET nome = ?, telefone = ?, whatsapp = ? WHERE id = ?";
    private static final String DELETE_FORNECEDOR = "DELETE FROM fornecedor WHERE id = ?";

    public void insert(Fornecedor fornecedor) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_FORNECEDOR, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTelefone());
            ps.setString(3, fornecedor.getWhatsapp());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir fornecedor: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir fornecedor", e);
        }
    }

    public Fornecedor findById(Long id) {
        Fornecedor fornecedor = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_FORNECEDOR_BY_ID)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fornecedor = mapResultSetToFornecedor(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedor por ID: " + e.getMessage());
        }
        return fornecedor;
    }

    public List<Fornecedor> findAll() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_FORNECEDORES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(mapResultSetToFornecedor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os fornecedores: " + e.getMessage());
        }
        return fornecedores;
    }

    public void update(Fornecedor fornecedor) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_FORNECEDOR)) {

            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTelefone());
            ps.setString(3, fornecedor.getWhatsapp());
            ps.setLong(4, fornecedor.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fornecedor: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar fornecedor", e);
        }
    }

    public void delete(Long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_FORNECEDOR)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar fornecedor: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar fornecedor", e);
        }
    }

    private Fornecedor mapResultSetToFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getLong("id"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setTelefone(rs.getString("telefone"));
        fornecedor.setWhatsapp(rs.getString("whatsapp"));
        return fornecedor;
    }
}
