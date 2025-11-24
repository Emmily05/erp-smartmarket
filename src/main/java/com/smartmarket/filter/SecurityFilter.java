package com.smartmarket.filter;

import com.smartmarket.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("*.xhtml")
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();


        if (uri.contains("login.xhtml") ||
            uri.contains("/jakarta.faces.resource/") ||
            uri.contains("/javax.faces.resource/") ||
            uri.endsWith(".css") || uri.endsWith(".js") ||
            uri.endsWith(".png") || uri.endsWith(".jpg") ||
            uri.endsWith(".ico") || uri.endsWith(".woff2") ||
            uri.endsWith(".ttf") || uri.equals(contextPath + "/")) {

            chain.doFilter(request, response);
            return;
        }


        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

        if (usuario == null) {
            response.sendRedirect(contextPath + "/login.xhtml");
            return;
        }


        String path = uri.substring(contextPath.length());

        // Só ADMIN e GERENTE acessam Produtos e Fornecedores
        if ((path.contains("/produtos.xhtml") || path.contains("/fornecedores.xhtml")) || path.contains("/relatorioVendas.xhtml")&
            !(usuario.getPerfil() == Usuario.Perfil.ADMIN || usuario.getPerfil() == Usuario.Perfil.GERENTE)) {
            response.sendRedirect(contextPath + "/dashboard.xhtml");
            return;
        }

        // Só ADMIN acessa Logs
        if (path.contains("/logs.xhtml") && usuario.getPerfil() != Usuario.Perfil.ADMIN) {
            response.sendRedirect(contextPath + "/dashboard.xhtml");
            return;
        }


        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}