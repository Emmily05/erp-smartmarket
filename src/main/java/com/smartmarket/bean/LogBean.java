package com.smartmarket.bean;

import com.smartmarket.model.LogSistema;
import com.smartmarket.service.LogSistemaService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class LogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LogSistemaService logSistemaService = new LogSistemaService();
    private List<LogSistema> logs;

    @PostConstruct
    public void init() {
        logs = logSistemaService.findAll();
    }

    public List<LogSistema> getLogs() {
        return logs;
    }
}
