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

## Future Relational Database

The design favors replacing infrastructure without changing use cases:

```text
domain.repository -> contracts used by services
infrastructure.repository.memory -> temporary implementation
infrastructure.repository.database -> future implementation
```

When the database topic enters the course, the implementation should avoid SQL built through string concatenation. With Spring Data JPA this is naturally handled through repositories and parameters. With JDBC, the adequate path is `PreparedStatement` or equivalent APIs.

## Future Frontend

The current frontend already consumes the real API, but remains simple. If the project advances to a richer interface, the `frontend` folder can receive a build-based application without changing the backend domain or services.
