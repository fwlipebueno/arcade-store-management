<a id="english"></a>

<p align="right">
  <kbd><a href="#english">English</a></kbd>
  <kbd><a href="#portuguese">Português</a></kbd>
  <kbd><a href="../README.md#english">Main README</a></kbd>
</p>

# Technical Notes

## Academic Alignment
The refactoring preserves the core model already delivered in the course materials:

- A customer performs sales.
- A category classifies products.
- A product has price, description and stock quantity.
- A sale has a customer, date/time and items.
- A sale item references product, quantity and unit price.
- Inventory centralizes stock availability, reservation and release.

The original requirements, class diagram and use-case diagram were produced in Portuguese. The codebase is standardized in English to follow the good-practice guidance of using a single language across the project.

## MVC Organization
The project follows MVC as a practical teaching structure:

- View: the `frontend` folder, responsible for screen structure, styling and browser interactions.
- Controller: REST controllers in `backend/src/main/java/br/edu/felipebueno/arcade/api/controller`, responsible for receiving HTTP requests and returning DTO responses.
- Model: domain classes, services and repositories in `domain`, `application` and `infrastructure`, responsible for business state, rules and persistence access.

This keeps the presentation layer away from business rules and keeps controllers thin enough to explain during a POO2 presentation.

## Security And Consistency
The application validates data in two layers:

- DTOs at the HTTP boundary, with Bean Validation.
- Domain classes, with business exceptions for essential rules.

Centralized error handling avoids exposing stack traces and standardizes API error responses.

Because there is no database yet, sale consistency is protected at the level currently possible:

- repeated items in the same sale are grouped;
- all products are validated before any stock reduction;
- sale registration is synchronized in the in-memory implementation to reduce accidental race conditions;
- customers and products linked to sales cannot be deleted;
- the future database implementation should use transactions.

## Current Persistence And Future Relational Database
In-memory persistence is intentional at this stage. It matches the current course moment because SQL and database mapping have not been formally covered yet, while still allowing the project to execute real use cases and tests.

The design favors replacing infrastructure without changing use cases:

```text
domain.repository -> contracts used by services
infrastructure.repository.memory -> temporary implementation
infrastructure.repository.database -> future implementation
```

When the database topic enters the course, the implementation should avoid SQL built through string concatenation. With Spring Data JPA this is naturally handled through repositories and parameters. With JDBC, the adequate path is `PreparedStatement` or equivalent APIs.

## Future Frontend
The current frontend already consumes the real API, but remains simple. If the project advances to a richer interface, the `frontend` folder can receive a build-based application without changing the backend domain or services.

---

<a id="portuguese"></a>

<p align="right">
  <kbd><a href="#english">English</a></kbd>
  <kbd><a href="#portuguese">Português</a></kbd>
  <kbd><a href="../README.md#portuguese">README principal</a></kbd>
</p>

# Notas Técnicas

## Alinhamento Acadêmico
A refatoração preserva o núcleo já entregue nos materiais da disciplina:

- Um cliente realiza vendas.
- Uma categoria classifica produtos.
- Um produto possui preço, descrição e quantidade em estoque.
- Uma venda possui cliente, data/hora e itens.
- Um item de venda referencia produto, quantidade e preço unitário.
- O estoque centraliza disponibilidade, reserva e devolução.

Os requisitos, o diagrama de classes e o diagrama de casos de uso originais foram produzidos em português. O código foi padronizado em inglês para seguir a orientação de boas práticas sobre usar um único idioma no projeto.

## Organização MVC
O projeto segue MVC como uma estrutura didática e prática:

- View: a pasta `frontend`, responsável pela estrutura da tela, estilo e interações no navegador.
- Controller: os controllers REST em `backend/src/main/java/br/edu/felipebueno/arcade/api/controller`, responsáveis por receber requisições HTTP e retornar DTOs.
- Model: classes de domínio, services e repositórios em `domain`, `application` e `infrastructure`, responsáveis pelo estado de negócio, regras e acesso à persistência.

Essa separação mantém a camada visual longe das regras de negócio e deixa os controllers enxutos o suficiente para explicar em uma apresentação de POO2.

## Segurança E Consistência
A aplicação valida dados em duas camadas:

- DTOs na entrada HTTP, com Bean Validation.
- Classes de domínio, com exceções de negócio para regras essenciais.

O tratamento centralizado de erros evita exposição de stack trace e padroniza as respostas da API.

Como ainda não há banco de dados, a consistência da venda foi protegida no nível possível:

- itens repetidos na mesma venda são agrupados;
- todos os produtos são validados antes de qualquer baixa de estoque;
- o registro de venda é sincronizado na implementação em memória para reduzir condições de corrida acidentais;
- clientes e produtos vinculados a vendas não podem ser removidos;
- a futura implementação com banco deve usar transações.

## Persistência Atual E Banco Relacional Futuro
A persistência em memória é intencional nesta etapa. Ela combina com o momento atual da disciplina porque SQL e mapeamento com banco ainda não foram formalmente trabalhados, mas ainda permite executar casos de uso reais e testes.

O desenho favorece a troca da infraestrutura sem alterar os casos de uso:

```text
domain.repository -> contratos usados pelos services
infrastructure.repository.memory -> implementação temporária
infrastructure.repository.database -> implementação futura
```

Quando banco de dados entrar na disciplina, a implementação deve evitar SQL montado por concatenação de strings. Com Spring Data JPA isso é tratado naturalmente por meio de repositórios e parâmetros. Com JDBC, o caminho adequado é `PreparedStatement` ou APIs equivalentes.

## Frontend Futuro
O frontend atual já consome a API real, mas permanece simples. Se o projeto avançar para uma interface mais completa, a pasta `frontend` pode receber uma aplicação com build próprio sem alterar o domínio ou os services do backend.
