package com.smartmarket.bean;

import com.smartmarket.model.Usuario;
import com.smartmarket.service.UsuarioService;
import com.smartmarket.util.FacesUtil;
import com.smartmarket.util.SessionUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private String emailRecuperacao;
    private Usuario usuarioLogado;
    private final UsuarioService usuarioService = new UsuarioService();

    
    public void login() {
        usuarioLogado = usuarioService.login(email, senha);

        if (usuarioLogado != null) {
            SessionUtil.setUsuarioLogado(usuarioLogado);
            FacesUtil.addInfoMessage("Bem-vindo(a)", "Olá, " + usuarioLogado.getNome() + "!");
            try {
                FacesUtil.redirect("/dashboard.xhtml");
            } catch (Exception e) {
                FacesUtil.addErrorMessage("Erro", "Não foi possível redirecionar.");
            }
        } else {
            FacesUtil.addErrorMessage("Login inválido", "E-mail ou senha incorretos.");
            email = null;
            senha = null;
        }
    }

    public void recuperarSenha() {
        if (emailRecuperacao == null || emailRecuperacao.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Erro", "O campo E-mail é obrigatório.");
            return;
        }

        String novaSenha = "123456"; // senha temporária

        if (usuarioService.resetarSenha(emailRecuperacao.trim(), novaSenha)) {
            FacesUtil.addInfoMessage("Sucesso!", 
                "Senha temporária gerada: <strong>" + novaSenha + "</strong><br/>"
              + "Um e-mail foi enviado para " + emailRecuperacao);
            emailRecuperacao = null;
        } else {
            FacesUtil.addErrorMessage("Erro", "E-mail não encontrado no sistema.");
        }
    }

    public String logout() {
        SessionUtil.invalidateSession();
        usuarioLogado = null;
        FacesUtil.addInfoMessage("Até logo!", "Você saiu do sistema.");
        return "/login.xhtml?faces-redirect=true";
    }


    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmailRecuperacao() { return emailRecuperacao; }
    public void setEmailRecuperacao(String emailRecuperacao) { this.emailRecuperacao = emailRecuperacao; }

    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }

    public boolean isLogado() { return usuarioLogado != null; }
}