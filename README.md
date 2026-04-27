<a id="english"></a>

<p align="right">
  <kbd><a href="#english">English</a></kbd>
  <kbd><a href="#portuguese">Português</a></kbd>
</p>

# Arcade Store Management
Arcade store management system developed for the POO2 course.

The project organizes the core operations of an arcade store: customers, categories, products, inventory and sales. The codebase was refactored to Spring Boot while preserving the class model, use cases and requirements already delivered for the course.

The original academic documents were produced in Portuguese. The implementation, API, documentation and frontend technical naming are now standardized in English to follow the course guidance about using a single language and to make the project stronger as a portfolio piece.

## Purpose
Build a simple, didactic and sustainable application to:

- create, query, update and delete customers;
- create, query, update and delete categories;
- create, query, update and delete products;
- manage stock in and stock out operations;
- register sales linked to customers;
- calculate sale items, subtotals and totals;
- query sale history and sales summary.

## Repository Structure
```text
arcade-store-management/
  backend/
    pom.xml
    src/main/java/br/edu/felipebueno/arcade/
      api/
        controller/
        dto/
        exception/
      application/
        service/
      config/
      domain/
        exception/
        model/
        repository/
      infrastructure/
        repository/memory/
    src/test/java/br/edu/felipebueno/arcade/
  frontend/
    assets/
      app-icon.webp
      favicon.png
      apple-touch-icon.png
      ambient-frame.webp
      hero-arcade-dashboard.webp
    src/
      api.js
      app.js
      ui.js
    index.html
    styles.css
    README.md
  docs/
    database/
      schema.sql
    technical-notes.md
```

## Architecture
The backend uses four practical responsibilities, without decorative layers:

- `domain`: business classes and repository contracts.
- `application`: use-case rules such as registration, inventory and sales.
- `api`: REST controllers, DTOs and HTTP error handling.
- `infrastructure`: technical persistence implementation, currently in memory.

The domain preserves the main classes from the academic model:

- `Customer`
- `Category`
- `Product`
- `Sale`
- `SaleItem`
- `Inventory`

## Backend
Technologies:

- Java 17
- Spring Boot 3.5.14
- Maven
- Bean Validation
- JUnit 5

Run:

```bash
cd backend
mvn spring-boot:run
```

Test:

```bash
cd backend
mvn clean test
```

Base API:

```text
http://localhost:8080/api
```

Main routes:

```text
GET    /api/customers
POST   /api/customers
GET    /api/customers/{id}
PUT    /api/customers/{id}
DELETE /api/customers/{id}

GET    /api/categories
POST   /api/categories
GET    /api/categories/{id}
PUT    /api/categories/{id}
DELETE /api/categories/{id}

GET    /api/products
POST   /api/products
GET    /api/products/{id}
PUT    /api/products/{id}
DELETE /api/products/{id}
POST   /api/products/{id}/stock/in
POST   /api/products/{id}/stock/out

GET    /api/sales
POST   /api/sales
GET    /api/sales/{id}
GET    /api/sales/report
```

## Frontend
The initial frontend is in `frontend/` and uses plain HTML, CSS and JavaScript. It is a real interface for registration, querying, inventory adjustment and sales, while avoiding a larger stack before the course consolidates database and integration requirements.

The frontend is intentionally small:

- `index.html`: view structure.
- `styles.css`: visual identity, responsive layout and lightweight motion.
- `assets/`: optimized raster assets for the app icon, favicon, background texture and hero visual.
- `src/api.js`: backend API calls.
- `src/ui.js`: DOM rendering and visual feedback.
- `src/app.js`: screen state and user interactions.

Run from the `frontend` folder:

```bash
python -m http.server 5500
```

Open:

```text
http://localhost:5500
```

## Database
The database has not been implemented yet because this stage has not been formalized in class. The structure is already prepared:

- services depend on repository contracts;
- the current implementation is isolated in `infrastructure/repository/memory`;
- a future database implementation can replace the in-memory repositories;
- the sale flow is shaped to become transactional;
- `docs/database/schema.sql` documents an initial relational model aligned with the domain.

When the database topic is covered in class, the natural path is adding Spring Data JPA or JDBC according to the professor's guidance.

## Important Decisions
### Monorepo
Backend, frontend and documentation remain in the same repository because the project is still small, academic and integrated. Splitting repositories now would add coordination without a real benefit.

### In-Memory Persistence
In-memory persistence keeps the system executable before the database stage. It is enough to validate business rules and demonstrate the main flows, but it does not replace real transactional integrity.

### Sales And Inventory
Sale registration validates all items before reducing stock. If one product lacks stock, no partial stock reduction is applied.

Customers and products already linked to sales cannot be deleted through the API. This preserves commercial history and prepares the project for the foreign key constraints expected in a relational database.

### DTOs And Validation
Controllers receive DTOs with explicit validation. The domain also protects its own rules so invalid data cannot bypass the API layer later.

### Error Handling
Business errors, missing resources and invalid data are handled centrally in `ApiExceptionHandler`.

## Evolution
Planned next steps:

1. Implement real persistence according to the course content.
2. Add transactions to sale registration.
3. Improve sales reporting.
4. Evolve the frontend into a build-based application if it becomes justified.
5. Add authentication and attendant/manager roles if they enter the project scope.

## Points That Depend On Future Classes
The following points still depend on course guidance:

- final choice between JDBC, JPA or another persistence approach;
- final entity mapping in the database;
- migration or table creation strategy;
- real transaction control;
- authentication, authorization and user roles;
- final interface format required by the course.

Until then, the base is ready to evolve without rewriting the core system.

---

<a id="portuguese"></a>

<p align="right">
  <kbd><a href="#english">English</a></kbd>
  <kbd><a href="#portuguese">Português</a></kbd>
</p>

# Arcade Store Management
Sistema de gerenciamento de loja arcade desenvolvido para a disciplina de POO2.

O projeto organiza as operações centrais de uma loja arcade: clientes, categorias, produtos, estoque e vendas. A base foi refatorada para Spring Boot preservando o modelo de classes, os casos de uso e os requisitos já entregues na disciplina.

Os documentos acadêmicos originais foram produzidos em português. O código, a API e a nomenclatura técnica do frontend foram padronizados em inglês para seguir a orientação de boas práticas da disciplina sobre usar um único idioma no projeto.

## Objetivo
Construir uma aplicação simples, didática e sustentável para:

- cadastrar, consultar, atualizar e remover clientes;
- cadastrar, consultar, atualizar e remover categorias;
- cadastrar, consultar, atualizar e remover produtos;
- controlar entrada e saída de estoque;
- registrar vendas vinculadas a clientes;
- calcular itens, subtotais e total da venda;
- consultar histórico e resumo de vendas.

## Estrutura Do Repositório
```text
arcade-store-management/
  backend/
    pom.xml
    src/main/java/br/edu/felipebueno/arcade/
      api/
        controller/
        dto/
        exception/
      application/
        service/
      config/
      domain/
        exception/
        model/
        repository/
      infrastructure/
        repository/memory/
    src/test/java/br/edu/felipebueno/arcade/
  frontend/
    assets/
      app-icon.webp
      favicon.png
      apple-touch-icon.png
      ambient-frame.webp
      hero-arcade-dashboard.webp
    src/
      api.js
      app.js
      ui.js
    index.html
    styles.css
    README.md
  docs/
    database/
      schema.sql
    technical-notes.md
```

## Arquitetura
O backend usa quatro responsabilidades práticas, sem camadas decorativas:

- `domain`: classes de negócio e contratos de repositório.
- `application`: regras de casos de uso, como cadastro, estoque e vendas.
- `api`: controllers REST, DTOs e tratamento de erros HTTP.
- `infrastructure`: implementação técnica de persistência, atualmente em memória.

O domínio preserva as classes principais do modelo acadêmico:

- `Customer`
- `Category`
- `Product`
- `Sale`
- `SaleItem`
- `Inventory`

## Backend
Tecnologias:

- Java 17
- Spring Boot 3.5.14
- Maven
- Bean Validation
- JUnit 5

Executar:

```bash
cd backend
mvn spring-boot:run
```

Testar:

```bash
cd backend
mvn clean test
```

API base:

```text
http://localhost:8080/api
```

Principais rotas:

```text
GET    /api/customers
POST   /api/customers
GET    /api/customers/{id}
PUT    /api/customers/{id}
DELETE /api/customers/{id}

GET    /api/categories
POST   /api/categories
GET    /api/categories/{id}
PUT    /api/categories/{id}
DELETE /api/categories/{id}

GET    /api/products
POST   /api/products
GET    /api/products/{id}
PUT    /api/products/{id}
DELETE /api/products/{id}
POST   /api/products/{id}/stock/in
POST   /api/products/{id}/stock/out

GET    /api/sales
POST   /api/sales
GET    /api/sales/{id}
GET    /api/sales/report
```

## Frontend
O frontend inicial está em `frontend/` e usa HTML, CSS e JavaScript puro. Ele funciona como uma interface real para cadastros, consultas, ajuste de estoque e vendas, sem introduzir uma stack maior antes de a disciplina consolidar banco de dados e integração.

O frontend permanece propositalmente pequeno:

- `index.html`: estrutura da view.
- `styles.css`: identidade visual e responsividade.
- `assets/`: assets rasterizados otimizados para ícone, favicon, textura de fundo e visual hero.
- `src/api.js`: chamadas para a API do backend.
- `src/ui.js`: renderização no DOM e feedback visual.
- `src/app.js`: estado da tela e interações do usuário.

Executar a partir da pasta `frontend`:

```bash
python -m http.server 5500
```

Acessar:

```text
http://localhost:5500
```

## Banco De Dados
O banco ainda não foi implementado porque essa etapa não foi formalizada em aula. A estrutura, porém, já está preparada:

- os services dependem de contratos de repositório;
- a implementação atual fica isolada em `infrastructure/repository/memory`;
- uma futura implementação com banco poderá substituir os repositórios em memória;
- o fluxo de venda já foi desenhado para se tornar transacional;
- `docs/database/schema.sql` documenta uma modelagem relacional inicial alinhada ao domínio.

Quando banco de dados for trabalhado na disciplina, o caminho natural será adicionar Spring Data JPA ou JDBC conforme a orientação da professora.

## Decisões Importantes
### Monorepo
Backend, frontend e documentação permanecem no mesmo repositório porque o projeto ainda é pequeno, acadêmico e integrado. Separar repositórios agora aumentaria a coordenação sem ganho real.

### Persistência Em Memória
A persistência em memória mantém o sistema executável antes da etapa de banco de dados. Ela é suficiente para validar regras de negócio e demonstrar os fluxos principais, mas não substitui integridade transacional real.

### Vendas E Estoque
O registro de venda valida todos os itens antes de reduzir o estoque. Se um produto não tiver estoque suficiente, nenhuma baixa parcial é aplicada.

Clientes e produtos já vinculados a vendas não podem ser removidos pela API. Essa escolha preserva o histórico comercial e prepara o projeto para as restrições de chave estrangeira esperadas em um banco relacional.

### DTOs E Validação
Controllers recebem DTOs com validação explícita. O domínio também protege suas próprias regras para que dados inválidos não contornem a camada de API no futuro.

### Tratamento De Erros
Erros de negócio, recursos não encontrados e dados inválidos são tratados de forma centralizada em `ApiExceptionHandler`.

## Evolução
Próximas etapas previstas:

1. Implementar persistência real conforme o conteúdo da disciplina.
2. Adicionar transações ao registro de venda.
3. Melhorar relatórios de vendas.
4. Evoluir o frontend para uma aplicação com build próprio, se isso se justificar.
5. Adicionar autenticação e papéis de atendente/gerente se entrarem no escopo do projeto.

## Pontos Que Dependem Das Próximas Aulas
Ainda dependem de orientação da disciplina:

- escolha final entre JDBC, JPA ou outra abordagem de persistência;
- mapeamento final das entidades no banco;
- estratégia de criação ou migração de tabelas;
- controle transacional real;
- autenticação, autorização e papéis de usuário;
- formato final de interface exigido pela disciplina.

Até lá, a base está pronta para evoluir sem reescrever o núcleo do sistema.
