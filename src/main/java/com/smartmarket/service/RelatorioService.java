package com.smartmarket.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartmarket.util.DBConnection;

public class RelatorioService {

    // TOTAL VENDIDO HOJE
    public BigDecimal totalVendidoHoje() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM venda WHERE DATE(data_hora) = CURDATE()";
        return executarQueryBigDecimal(sql);
    }

    // TOTAL VENDIDO NO PERÍODO
    public BigDecimal totalVendidoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM venda WHERE data_hora BETWEEN ? AND ?";
        return executarQueryBigDecimal(sql, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }

    // TICKET MÉDIO NO PERÍODO
    public BigDecimal ticketMedio(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT COALESCE(AVG(total), 0) FROM venda WHERE data_hora BETWEEN ? AND ?";
        BigDecimal valor = executarQueryBigDecimal(sql, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
        return valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // TOTAL DE VENDAS (QUANTIDADE DE CUPONS)
    public int totalVendas(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT COUNT(*) FROM venda WHERE data_hora BETWEEN ? AND ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // TOP 10 PRODUTOS MAIS VENDIDOS 
    public List<Map<String, Object>> topProdutos(LocalDateTime inicio, LocalDateTime fim) {
        if (!tabelaExiste("venda_item")) {
            return new ArrayList<>();
        }

        String sql = """
            SELECT produto_nome AS nome,
                   SUM(quantidade) AS quantidade,
                   SUM(subtotal) AS total
            FROM venda_item vi
            JOIN venda v ON vi.venda_id = v.id
            WHERE v.data_hora BETWEEN ? AND ?
            GROUP BY produto_nome
            ORDER BY total DESC
            LIMIT 10
            """;

        return executarQueryList(sql, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }

    // VENDAS POR FUNCIONÁRIO
    public List<Map<String, Object>> vendasPorUsuario(LocalDateTime inicio, LocalDateTime fim) {
        String sql = """
            SELECT COALESCE(usuario_nome, 'Não identificado') AS usuario_nome,
                   COUNT(*) AS quantidade,
                   COALESCE(SUM(total), 0) AS total
            FROM venda
            WHERE data_hora BETWEEN ? AND ?
            GROUP BY usuario_nome
            ORDER BY total DESC
            """;

        return executarQueryList(sql, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }

    // VENDAS POR DIA
    public List<Map<String, Object>> vendasDiarias(LocalDateTime inicio, LocalDateTime fim) {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT DATE(data_hora) AS dia,
                   COUNT(*) AS vendas,
                   COALESCE(SUM(total), 0) AS total
            FROM venda
            WHERE data_hora BETWEEN ? AND ?
            GROUP BY DATE(data_hora)
            ORDER BY dia DESC
            """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(inicio));
            ps.setTimestamp(2, Timestamp.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> mapa = new HashMap<>();
                    mapa.put("dia", rs.getDate("dia"));
                    mapa.put("vendas", rs.getInt("vendas"));
                    mapa.put("total", rs.getBigDecimal("total"));
                    lista.add(mapa);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private BigDecimal executarQueryBigDecimal(String sql, Object... params) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private List<Map<String, Object>> executarQueryList(String sql, Object... params) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                java.sql.ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> mapa = new HashMap<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        mapa.put(md.getColumnLabel(i).toLowerCase(), rs.getObject(i));
                    }
                    lista.add(mapa);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private boolean tabelaExiste(String nomeTabela) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT 1 FROM information_schema.tables WHERE table_name = ?")) {
            ps.setString(1, nomeTabela);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return false;
        }
    }
}