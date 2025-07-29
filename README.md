# Estoque Service

Serviço Java baseado em Spring Boot para gerenciamento de estoque, utilizando PostgreSQL, Flyway para migrações e arquitetura hexagonal (Ports & Adapters).

---

## 🏗️ Arquitetura

O projeto segue o padrão de arquitetura hexagonal, separando as regras de negócio (domínio) das implementações externas (gateways/adapters):

- **API**: Endpoints REST para operações de estoque.
- **Domain**: Modelos, exceções e portas (interfaces) do domínio.
- **Gateway**: Implementações de acesso a dados (JPA) e clientes externos (ProdutoClient).
- **Service**: Lógica de negócio central.
- **Utils**: Utilitários e constantes.

---

## 📁 Estrutura de Pastas
```
src/main/java/com/fiap/postech/estoque_service/
├── api/
│   ├── controller/
│   │   └── EstoqueController.java
│   ├── dto/
│   │   ├── BaixaEstoqueRequest.java
│   │   ├── BaixaEstoqueResponse.java
│   │   ├── EstoqueDto.java
│   │   ├── EstoqueRequest.java
│   │   ├── ItemEstoqueBaixaDTO.java
│   │   └── ResponseDto.java
│   └── mapper/
│       └── EstoqueMapper.java
├── domain/
│   ├── exceptions/
│   │   ├── ErroInternoException.java
│   │   ├── GlobalHandlerException.java
│   │   └── internal/
│   │       ├── EstoqueExistsException.java
│   │       ├── EstoqueNotFoundException.java
│   │       ├── InvalidQuantidadeEstoqueException.java
│   │       ├── InvalidSkuEstoqueException.java
│   │       └── ProdutoNotFoundException.java
│   └── model/
│       └── Estoque.java
├── gateway/
│   ├── client/
│   │   ├── ProdutoClient.java
│   │   └── dto/
│   │       └── ProdutoDto.java
│   └── database/
│       ├── EstoqueRepositoryImpl.java
│       ├── entity/
│       │   └── EstoqueEntity.java
│       └── repository/
│           └── EstoqueRepositoryJPA.java
├── port/
│   ├── EstoqueRepositoryPort.java
│   └── EstoqueServicePort.java
├── service/
│   └── EstoqueServiceImpl.java
├── utils/
│   └── ConstantUtils.java
└── EstoqueServiceApplication.java

```
---

## 🧩 Principais Classes

- **EstoqueController**: Endpoints REST para operações de gerenciamento de estoque.
- **EstoqueServiceImpl**: Implementação da lógica de negócio para o domínio de estoque.
- **EstoqueRepositoryPort**: Interface para a implementação de persistência do estoque.
- **EstoqueRepositoryImpl**: Implementação do repositório usando JPA para acesso ao banco de dados.
- **EstoqueEntity**: Entidade JPA para persistência dos dados de estoque.
- **Estoque**: Modelo de domínio que representa o estoque.
- **ProdutoClient**: Cliente para comunicação com o serviço de produtos.
- **DTOs**: Objetos para transferência de dados entre as camadas da aplicação.
- **Exceções**: Tratamento centralizado de erros e validações da aplicação.

---

## ⚙️ Configuração

O arquivo `src/main/resources/application.yml` define:

- Conexão com PostgreSQL (ajustável por variáveis de ambiente).
- Flyway para migrações automáticas do banco de dados.
- JPA configurado para atualização automática do schema e exibição de SQL.
- Configuração do serviço de produto, com a URL do serviço de produto (ajustável por variável de ambiente).

---

## ▶️ Executando o Projeto

1. Configure o banco PostgreSQL e ajuste as variáveis de ambiente se necessário.
2. Execute as migrações Flyway automaticamente ao iniciar a aplicação.
3. Rode o projeto com: `mvn spring-boot:run`
4. A aplicação estará disponível em `http://localhost:8080`.

---

## 📚 Documentação

A documentação dos endpoints pode ser acessada via Swagger em `/swagger-ui.html` (caso habilitado).
