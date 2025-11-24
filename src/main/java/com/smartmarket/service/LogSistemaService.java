package com.smartmarket.service;

import com.smartmarket.dao.LogSistemaDAO;
import com.smartmarket.model.LogSistema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smartmarket.model.Usuario;
import com.smartmarket.util.SessionUtil;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class LogSistemaService {

    private static final Logger logger = LoggerFactory.getLogger(LogSistemaService.class);

    private LogSistemaDAO logSistemaDAO = new LogSistemaDAO();

    public void log(String acao, String detalhes) {
        Usuario usuario = SessionUtil.getUsuarioLogado();
        
        if (usuario == null) {
            logger.warn("Tentativa de log sem usuário logado. Ação: {}, Detalhes: {}", acao, detalhes);
            return;
        }

        logger.info("Usuário: {} ({}) - Ação: {} - Detalhes: {}", usuario.getNome(), usuario.getId(), acao, detalhes);
        
        LogSistema log = new LogSistema();
        log.setUsuarioId(usuario.getId());
        log.setUsuarioNome(usuario.getNome());
        log.setAcao(acao);
        log.setDetalhes(detalhes);
        
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            log.setIp(ipAddress);
        } catch (Exception e) {
            log.setIp("N/A");
        }
        
        logSistemaDAO.insert(log);
    }

    public List<LogSistema> findAll() {
        return logSistemaDAO.findAll();
    }
}
