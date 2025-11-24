package com.smartmarket.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Configurações do Banco de Dados
    private static final String URL = "jdbc:mysql://localhost:3306/smartmarket?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root"; // Altere para o seu usuário do MySQL
    private static final String PASSWORD = "Projeto.!?*005#"; // Altere para sua senha do MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados.
     * @return Objeto Connection
     * @throws SQLException Se ocorrer um erro de conexão.
     */
    public static Connection getConnection() throws SQLException { 
        try {
            // 1. Carrega o driver JDBC
            Class.forName(DRIVER);
            
            // 2. Estabelece a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado.");
            throw new SQLException("Driver JDBC não encontrado.", e);
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     * @param conn A conexão a ser fechada.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
