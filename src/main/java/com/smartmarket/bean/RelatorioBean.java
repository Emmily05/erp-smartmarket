package com.smartmarket.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.smartmarket.service.RelatorioService;

@Named
@SessionScoped
public class RelatorioBean implements Serializable {

    private final RelatorioService service = new RelatorioService();

    private String filtroRapido = "HOJE";
    private LocalDate dataInicio = LocalDate.now();
    private LocalDate dataFim = LocalDate.now();

    // métricas
    private BigDecimal totalHoje = BigDecimal.ZERO;
    private BigDecimal totalPeriodo = BigDecimal.ZERO;
    private BigDecimal ticketMedio = BigDecimal.ZERO;
    private int totalVendas = 0;

    private List<Map<String, Object>> topProdutos;
    private List<Map<String, Object>> vendasPorUsuario;
    private List<Map<String, Object>> vendasDiarias;  

    @PostConstruct
    public void init() {
        aplicarFiltroRapido();
    }

    public void aplicarFiltroRapido() {
        LocalDateTime inicio, fim;

        switch (filtroRapido) {
            case "HOJE" -> {
                inicio = LocalDate.now().atStartOfDay();
                fim = LocalDateTime.now();
            }
            case "ONTEM" -> {
                inicio = LocalDate.now().minusDays(1).atStartOfDay();
                fim = inicio.plusDays(1);
            }
            case "SETEDIAS" -> {
                inicio = LocalDate.now().minusDays(6).atStartOfDay();
                fim = LocalDateTime.now();
            }
            case "MES" -> {
                inicio = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                fim = LocalDateTime.now();
            }
            default -> {
                inicio = LocalDate.now().atStartOfDay();
                fim = LocalDateTime.now();
            }
        }

        dataInicio = inicio.toLocalDate();
        dataFim = fim.toLocalDate();
        filtrarPeriodo(inicio, fim);
    }

    public void filtrar() {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59);
        filtrarPeriodo(inicio, fim);
    }

    private void filtrarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        totalHoje = service.totalVendidoHoje();
        totalPeriodo = service.totalVendidoPeriodo(inicio, fim);
        ticketMedio = service.ticketMedio(inicio, fim);
        totalVendas = service.totalVendas(inicio, fim);
        topProdutos = service.topProdutos(inicio, fim);
        vendasPorUsuario = service.vendasPorUsuario(inicio, fim);
        vendasDiarias = service.vendasDiarias(inicio, fim);  
    }


    public String getFiltroRapido() { return filtroRapido; }
    public void setFiltroRapido(String filtroRapido) { this.filtroRapido = filtroRapido; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public BigDecimal getTotalHoje() { return totalHoje; }
    public BigDecimal getTotalPeriodo() { return totalPeriodo; }
    public BigDecimal getTicketMedio() { return ticketMedio; }
    public int getTotalVendas() { return totalVendas; }
    public List<Map<String, Object>> getTopProdutos() { return topProdutos; }
    public List<Map<String, Object>> getVendasPorUsuario() { return vendasPorUsuario; }
    public List<Map<String, Object>> getVendasDiarias() { return vendasDiarias; }  
}