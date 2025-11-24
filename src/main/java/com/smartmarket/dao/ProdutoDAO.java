package com.smartmarket.dao;

import com.smartmarket.model.Produto;
import com.smartmarket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends GenericDAO {

    private static final String INSERT_PRODUTO = "INSERT INTO produto (codigo, nome, preco, estoque, estoque_minimo, fornecedor_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_PRODUTOS = "SELECT * FROM produto";
    private static final String SELECT_PRODUTOS_ESTOQUE_CRITICO = "SELECT * FROM produto WHERE estoque < estoque_minimo AND ativo = TRUE";
    private static final String UPDATE_PRODUTO = "UPDATE produto SET codigo = ?, nome = ?, preco = ?, estoque = ?, estoque_minimo = ?, fornecedor_id = ?, ativo = ? WHERE id = ?";
    private static final String DELETE_PRODUTO = "DELETE FROM produto WHERE id = ?";
    
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public void insert(Produto produto) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_PRODUTO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produto.getCodigo());
            ps.setString(2, produto.getNome());
            ps.setBigDecimal(3, produto.getPreco());
            ps.setBigDecimal(4, produto.getEstoque());
            ps.setBigDecimal(5, produto.getEstoqueMinimo());
            ps.setLong(6, produto.getFornecedorId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir produto", e);
        }
    }

    public List<Produto> findAll() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_PRODUTOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os produtos: " + e.getMessage());
        }
        return produtos;
    }
    
    public List<Produto> findEstoqueCritico() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_PRODUTOS_ESTOQUE_CRITICO);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos com estoque crítico: " + e.getMessage());
        }
        return produtos;
    }

    public void update(Produto produto) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_PRODUTO)) {

            ps.setString(1, produto.getCodigo());
            ps.setString(2, produto.getNome());
            ps.setBigDecimal(3, produto.getPreco());
            ps.setBigDecimal(4, produto.getEstoque());
            ps.setBigDecimal(5, produto.getEstoqueMinimo());
            ps.setLong(6, produto.getFornecedorId());
            ps.setBoolean(7, produto.isAtivo());
            ps.setLong(8, produto.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }

    public void delete(Long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_PRODUTO)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar produto", e);
        }
    }

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setEstoque(rs.getBigDecimal("estoque"));
        produto.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
        produto.setFornecedorId(rs.getLong("fornecedor_id"));
        produto.setAtivo(rs.getBoolean("ativo"));
        

        if (produto.getFornecedorId() != 0) {
            produto.setFornecedor(fornecedorDAO.findById(produto.getFornecedorId()));
        }
        
        return produto;
    }
    
    public List<Produto> findAtivos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = true ORDER BY nome";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getBigDecimal("preco"));
                p.setEstoque(rs.getBigDecimal("estoque"));
                p.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
                p.setFornecedorId(rs.getLong("fornecedor_id"));
                p.setAtivo(rs.getBoolean("ativo"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Produto> findInativos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = false ORDER BY nome";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getBigDecimal("preco"));
                p.setEstoque(rs.getBigDecimal("estoque"));
                p.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
                p.setFornecedorId(rs.getLong("fornecedor_id"));
                p.setAtivo(rs.getBoolean("ativo"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public Produto findById(Long id) {
        Produto p = null;
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Produto();
                    p.setId(rs.getLong("id"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNome(rs.getString("nome"));
                    p.setPreco(rs.getBigDecimal("preco"));
                    p.setEstoque(rs.getBigDecimal("estoque"));
                    p.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
                    p.setFornecedorId(rs.getLong("fornecedor_id"));
                    p.setAtivo(rs.getBoolean("ativo"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
