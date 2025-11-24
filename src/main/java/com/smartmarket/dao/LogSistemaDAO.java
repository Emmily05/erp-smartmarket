package com.smartmarket.dao;

import com.smartmarket.model.LogSistema;
import com.smartmarket.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogSistemaDAO extends GenericDAO {

    private static final Logger logger = LoggerFactory.getLogger(LogSistemaDAO.class);

    private static final String INSERT_LOG = "INSERT INTO log_sistema (usuario_id, usuario_nome, acao, detalhes, ip) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_LOGS = "SELECT * FROM log_sistema ORDER BY data_hora DESC";

    public void insert(LogSistema log) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_LOG)) {

            ps.setLong(1, log.getUsuarioId());
            ps.setString(2, log.getUsuarioNome());
            ps.setString(3, log.getAcao());
            ps.setString(4, log.getDetalhes());
            ps.setString(5, log.getIp());

            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Erro ao inserir log no banco de dados.", e);
        }
    }

    public List<LogSistema> findAll() {
        List<LogSistema> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_LOGS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }

        } catch (SQLException e) {
            logger.error("Erro ao buscar todos os logs no banco de dados.", e);
        }
        return logs;
    }

    private LogSistema mapResultSetToLog(ResultSet rs) throws SQLException {
        LogSistema log = new LogSistema();
        log.setId(rs.getLong("id"));
        log.setUsuarioId(rs.getLong("usuario_id"));
        log.setUsuarioNome(rs.getString("usuario_nome"));
        log.setAcao(rs.getString("acao"));
        log.setDetalhes(rs.getString("detalhes"));
        log.setIp(rs.getString("ip"));
        log.setDataHora(getLocalDateTime(rs, "data_hora"));
        return log;
    }
}
