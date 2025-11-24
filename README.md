# SmartMarket Mini - Projeto Web Completo (JSF/PrimeFaces + JDBC/MySQL)

Este é o projeto **SmartMarket Mini** completo, implementado como uma aplicação web utilizando **Jakarta EE (antigo Java EE)**, **JSF (Jakarta Server Faces)**, **PrimeFaces** para a interface e **JDBC** para a persistência com **MySQL**.

O projeto **não utiliza Spring Boot**, conforme solicitado, e está configurado como um projeto Maven Webapp para ser facilmente importado e executado no Eclipse com um servidor Tomcat.

## 1. Pré-requisitos

Para rodar este projeto, você precisará ter instalado:

1.  **Java Development Kit (JDK) 17** ou superior.
2.  **MySQL Server** (versão 8.0 recomendada).
3.  **MySQL Workbench** ou outro cliente SQL.
4.  **Eclipse IDE** (com suporte a projetos Maven e Web).
5.  **Apache Tomcat 10.1** (ou superior) configurado no Eclipse.

## 2. Configuração do Banco de Dados MySQL

O projeto espera que exista um banco de dados chamado `smartmarket` e que o usuário de conexão seja `root` com a senha `root`.

**Passo a passo:**

1.  Abra o seu cliente MySQL.
2.  Execute o script SQL fornecido no arquivo `db_script.sql` para criar o banco de dados, todas as tabelas (`usuario`, `fornecedor`, `produto`, `venda`, `item_venda`, `log_sistema`) e inserir os dados iniciais.

    ```sql
    -- Exemplo de comandos a serem executados:
    SOURCE /caminho/para/smartmarket/db_script.sql;
    ```

3.  **Verifique as credenciais:**
    *   **Banco de Dados:** `smartmarket`
    *   **Usuário:** `root`
    *   **Senha:** `root`

    **IMPORTANTE:** Se suas credenciais do MySQL forem diferentes, você **DEVE** alterar o arquivo `smartmarket/src/main/java/com/smartmarket/util/DBConnection.java` nas linhas 10 e 11:

    ```java
    private static final String USER = "seu_usuario"; // Altere aqui
    private static final String PASSWORD = "sua_senha"; // Altere aqui
    ```

## 3. Configuração e Execução no Eclipse

O projeto utiliza **Maven** para gerenciar as dependências.

**Passo a passo:**

1.  **Importar o Projeto:**
    *   No Eclipse, vá em `File` -> `Import...`
    *   Selecione `Maven` -> `Existing Maven Projects`.
    *   Clique em `Browse...` e selecione a pasta raiz do projeto (`smartmarket`).
    *   Clique em `Finish`.

2.  **Configurar o Tomcat:**
    *   Certifique-se de ter o **Apache Tomcat 10.1** (ou superior) configurado no Eclipse (pois o projeto usa Jakarta EE 10).
    *   Clique com o botão direito no projeto `smartmarket` no Package Explorer.
    *   Vá em `Properties` -> `Targeted Runtimes` e selecione o seu Tomcat 10.1.

3.  **Executar o Projeto:**
    *   Clique com o botão direito no projeto `smartmarket`.
    *   Selecione `Run As` -> `Run on Server`.
    *   Escolha o seu servidor Tomcat 10.1.

### Acesso ao Sistema

Após o Tomcat iniciar, o sistema estará acessível em: `http://localhost:8080/smartmarket/login.xhtml`

**Credenciais de Teste (Senha: `123` para todos):**

| Perfil | Email |
| :--- | :--- |
| **Administrador** | `admin@smart.com` |
| **Gerente** | `maria@smart.com` |
| **Caixa** | `joao@smart.com` |

## 4. Funcionalidades Implementadas

| Funcionalidade | Descrição |
| :--- | :--- |
| **Login** | Tela de login com autenticação no banco de dados e controle de sessão. |
| **Dashboard** | Tela inicial que exibe o **Aviso de Estoque Baixo** (tabela vermelha e popup). |
| **Cadastro de Produtos** | CRUD completo com campos para código, preço, estoque, estoque mínimo e fornecedor. |
| **Cadastro de Fornecedores** | CRUD simples de fornecedores. |
| **PDV (Caixa)** | Simulação de Ponto de Venda com adição de produtos por código, cálculo de total e troco. |
| **Cupom Fiscal** | Geração de um arquivo de texto (`.txt`) simulando o cupom fiscal com os dados da venda. |
| **Logs** | Tela de Logs de Auditoria (`logs.xhtml`) que registra as ações do sistema. |

---
*Projeto criado por **Manus AI**.*
