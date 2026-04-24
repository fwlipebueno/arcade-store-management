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
    index.html
    styles.css
    app.js
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
mvn test
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
The initial frontend is in `frontend/` and uses plain HTML, CSS and JavaScript. It is a real interface for registration, querying and sales, while avoiding a larger stack before the course consolidates database and integration requirements.

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
