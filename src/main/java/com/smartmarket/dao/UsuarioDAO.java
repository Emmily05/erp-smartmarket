package com.smartmarket.dao;

import com.smartmarket.model.Usuario;
import com.smartmarket.model.Usuario.Perfil;
import com.smartmarket.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends GenericDAO {

    private static final String INSERT_USUARIO = "INSERT INTO usuario (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_USUARIOS = "SELECT * FROM usuario";
    private static final String SELECT_USUARIO_BY_EMAIL = "SELECT * FROM usuario WHERE email = ?";
    private static final String SELECT_USUARIO_BY_ID = "SELECT * FROM usuario WHERE id = ?";
    private static final String UPDATE_USUARIO = "UPDATE usuario SET nome = ?, email = ?, perfil = ?, ativo = ? WHERE id = ?";
    private static final String DELETE_USUARIO = "DELETE FROM usuario WHERE id = ?";

    public void insert(Usuario usuario) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getPerfil().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir usuário", e);
        }
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USUARIO_BY_EMAIL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapResultSetToUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }
        return usuario;
    }
    
    public Usuario findById(Long id) {
        Usuario usuario = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USUARIO_BY_ID)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapResultSetToUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return usuario;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USUARIOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os usuários: " + e.getMessage());
        }
        return usuarios;
    }

    public void update(Usuario usuario) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USUARIO)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPerfil().name());
            ps.setBoolean(4, usuario.isAtivo());
            ps.setLong(5, usuario.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    public void delete(Long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USUARIO)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setPerfil(Perfil.valueOf(rs.getString("perfil")));
        usuario.setAtivo(rs.getBoolean("ativo"));
        usuario.setCriadoEm(getLocalDateTime(rs, "criado_em"));
        return usuario;
    }
}
