package com.smartmarket.service;

import com.smartmarket.dao.UsuarioDAO;
import com.smartmarket.model.Usuario;
import com.smartmarket.util.BCryptUtil;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario != null && usuario.isAtivo()) {

            	if (senha.equals(usuario.getSenha())) {
                return usuario;
            } 
        }
        return null;
    }
    
    public void save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setSenha(BCryptUtil.hashPassword(usuario.getSenha()));
            usuarioDAO.insert(usuario);
        } else {
            usuarioDAO.update(usuario);
        }
    }

    public boolean resetarSenha(String email, String novaSenha) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario != null) {

            usuario.setSenha(BCryptUtil.hashPassword(novaSenha));
            usuarioDAO.update(usuario);
            return true;
        }
        return false;
    }
    
    public Usuario findByEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }
}