# MiniPar Service (minipar-service)

## 📜 Visão Geral

Este projeto implementa a camada de **serviço web (API REST)** para o compilador/interpretador da linguagem MiniPar. Ele expõe as funcionalidades principais do MiniPar (interpretação e compilação para Assembly ARMv7) através de uma API HTTP, permitindo que diferentes clientes (como a `MiniParGUI` ou ferramentas de linha de comando) consumam essas funcionalidades via rede.

Este serviço adota o padrão **Compilador como Serviço (CaaS)**, atuando como uma *Facade* sobre a biblioteca `minipar-core`.

## 🏛️ Arquitetura

O `minipar-service` é um dos componentes principais na arquitetura CaaS do MiniPar:

1.  **Clientes (Externos):** Aplicações como `MiniParGUI` ou `Main.runTerminal` (que residem no `minipar-core`).
2.  **`minipar-service` (Este Repositório):** Uma aplicação **Spring Boot** que recebe requisições HTTP.
3.  **`minipar-core` (Dependência):** A biblioteca Java (`.jar`) que contém toda a lógica do Lexer, Parser, Analisador Semântico, Interpretador, Gerador de IR e Gerador de Assembly. O `minipar-service` utiliza o `minipar-core` como uma dependência Maven.

O fluxo de execução é: Cliente → HTTP Request → `minipar-service` → Chamada Java para `minipar-core` → `minipar-core` processa → `minipar-service` → HTTP Response → Cliente.

## ✨ Funcionalidades Principais

* Expõe a lógica do `minipar-core` via API REST.
* Recebe código-fonte MiniPar e a variante desejada (interpretar ou compilar).
* Retorna a saída da execução (modo interpretador) ou os códigos gerados (IR e Assembly - modo compilador) em formato JSON.
* Trata erros durante o processamento e retorna mensagens de erro padronizadas.

## 🛠️ Tecnologias Utilizadas

* **Java (JDK 22)**
* **Spring Boot (com Spring Web):** Para criação rápida da API REST e servidor web embarcado (Tomcat).
* **Apache Maven:** Para gerenciamento do projeto e dependências (incluindo a dependência do `minipar-core`).
* **Jackson:** Para serialização/deserialização de JSON (integrado ao Spring Boot).

## 🔌 API Endpoint

O serviço expõe um endpoint principal:

* **`POST /run`**
    * **Content-Type:** `application/json`
    * **Request Body (JSON):**
        ```json
        {
          "code": "programa_minipar\nSEQ\n  n = 5\n  print(n)",
          "variant": "interpret" // ou "compile"
        }
        ```
    * **Response Body (JSON - Sucesso):**
        ```json
        // Exemplo para variant="interpret"
        {
          "success": true,
          "output": "5.0\n",
          "irCode": null,
          "assemblyCode": null,
          "error": null
        }
        
        // Exemplo para variant="compile"
        {
          "success": true,
          "output": "Compilação concluída com sucesso.",
          "irCode": "\tn = 5\n\tPRINT n\n",
          "assemblyCode": ".global _start\n\n.data\nn: .word 0\n\n.text\n_start:\n\tMOV R0, #5\n\tLDR R10, =n\n...",
          "error": null
        }
        ```
    * **Response Body (JSON - Erro):**
        ```json
        {
          "success": false,
          "output": null,
          "irCode": null,
          "assemblyCode": null,
          "error": "Erro ao executar: Erro sintatico na linha 3..." 
        }
        ```

## 🚀 Como Executar

1.  **Pré-requisito:** Certifique-se de que o projeto `minipar-core` (o compilador/interpretador) foi compilado e instalado no seu repositório Maven local. Navegue até a pasta raiz do `minipar-core` e execute:
    ```bash
    mvn clean install
    ```
2.  **Execute o Serviço:** Navegue até a pasta raiz deste projeto (`minipar-service`) e execute:
    ```bash
    mvn spring-boot:run
    ```
    Alternativamente, importe o projeto em sua IDE (IntelliJ, VS Code com extensões Java) e execute a classe `MiniparServiceApplication`.
3.  O serviço estará disponível em `http://localhost:8080` (porta padrão).

## 🔗 Dependências

* **`minipar-core`:** A biblioteca contendo a lógica do compilador/interpretador MiniPar e as classes DTO (`RunRequest`, `RunResponse`, `CompilerResult`).

---
