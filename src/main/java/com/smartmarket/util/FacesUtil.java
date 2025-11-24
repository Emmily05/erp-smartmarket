package com.smartmarket.util;

import java.io.IOException;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

public class FacesUtil {

    public static void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    public static void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }
    
    public static void redirect(String url) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExternalContext ec = ctx.getExternalContext();
            ec.redirect(ec.getRequestContextPath() + url);
        } catch (IOException e) {
            addErrorMessage("Erro de redirecionamento", e.getMessage());
        }
    }
}
