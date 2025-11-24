package com.smartmarket.util;

import com.smartmarket.model.Usuario;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String USER_SESSION_KEY = "usuarioLogado";

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static void setUsuarioLogado(Usuario usuario) {
        getSession().setAttribute(USER_SESSION_KEY, usuario);
    }

    public static Usuario getUsuarioLogado() {
        return (Usuario) getSession().getAttribute(USER_SESSION_KEY);
    }

    public static void invalidateSession() {
        HttpSession session = getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isGerenteOrAdmin() {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) {
            return false;
        }
        return usuario.getPerfil() == Usuario.Perfil.ADMIN || usuario.getPerfil() == Usuario.Perfil.GERENTE;
    }

    public static boolean isCaixaOrAdmin() {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) {
            return false;
        }
        return usuario.getPerfil() == Usuario.Perfil.ADMIN || usuario.getPerfil() == Usuario.Perfil.CAIXA;
    }

    public static boolean isAdmin() {
        Usuario usuario = getUsuarioLogado();
        if (usuario == null) {
            return false;
        }
        return usuario.getPerfil() == Usuario.Perfil.ADMIN;
    }
}
