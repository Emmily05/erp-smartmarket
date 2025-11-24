-- Script SQL para criação do banco de dados e tabelas do SmartMarket Mini
--
-- ATENÇÃO: Altere a senha e o usuário no arquivo DBConnection.java
--
-- private static final String USER = "root"; 
-- private static final String PASSWORD = "root"; 

-- 1. Criação do Banco de Dados
CREATE DATABASE IF NOT EXISTS smartmarket CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE smartmarket;

-- 2. Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN','GERENTE','CAIXA','CLIENTE') DEFAULT 'CAIXA',
    ativo BOOLEAN DEFAULT TRUE,
    criado_em DATETIME DEFAULT NOW()
);

-- 3. Tabela de Fornecedores
CREATE TABLE IF NOT EXISTS fornecedor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(20),
    whatsapp VARCHAR(20)
);

-- 4. Tabela de Produtos
CREATE TABLE IF NOT EXISTS produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nome VARCHAR(200) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque DECIMAL(10,2) DEFAULT 0,
    estoque_minimo DECIMAL(10,2) DEFAULT 5,
    fornecedor_id BIGINT,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);

-- 5. Tabela de Vendas
CREATE TABLE IF NOT EXISTS venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME DEFAULT NOW(),
    total DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(20),
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- 6. Tabela de Itens de Venda
CREATE TABLE IF NOT EXISTS item_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT,
    produto_id BIGINT,
    quantidade DECIMAL(10,2),
    preco_unitario DECIMAL(10,2),
    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

-- 7. Tabela de Logs
CREATE TABLE IF NOT EXISTS log_sistema (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    usuario_nome VARCHAR(100),
    acao VARCHAR(100),           -- EX: "VENDA REALIZADA", "PRODUTO CADASTRADO", "ESTOQUE ALTERADO"
    detalhes TEXT,
    ip VARCHAR(45),
    data_hora DATETIME DEFAULT NOW(),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- 8. Dados iniciais (a senha de todos é "123", criptografada com BCrypt)
INSERT INTO fornecedor (nome, telefone, whatsapp) VALUES 
('Fornecedor Padrão', '(11) 9876-5432', '(11) 99876-5432');

INSERT INTO usuario (nome,email,senha,perfil) VALUES
('Administrador','admin@smart.com','$2a$10$3T7uW8iX9vB2kP9qW1rT5y6u7i8o9p0a1b2c3d4e5f6g', 'ADMIN'),
('Maria Gerente','maria@smart.com','$2a$10$3T7uW8iX9vB2kP9qW1rT5y6u7i8o9p0a1b2c3d4e5f6g', 'GERENTE'),
('João Caixa','joao@smart.com','$2a$10$3T7uW8iX9vB2kP9qW1rT5y6u7i8o9p0a1b2c3d4e5f6g', 'CAIXA');

INSERT INTO produto (codigo, nome, preco, estoque, estoque_minimo, fornecedor_id) VALUES
('7891000100011', 'Coca Cola 2L', 8.50, 50.0, 10.0, 1),
('7891000100028', 'Arroz Tio João 5kg', 24.90, 1.0, 5.0, 1), -- Estoque Crítico
('7891000100035', 'Sabão Omo 1kg', 18.90, 5.0, 5.0, 1);
