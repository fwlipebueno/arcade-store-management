# Arcade Store Management
Base inicial do projeto de POO2 para o sistema de gerenciamento de loja arcade.

## Objetivo do projeto
O sistema tem como objetivo apoiar o gerenciamento de produtos, clientes, vendas e estoque de uma loja arcade.

## Estrutura atual
- `app`: ponto de entrada do sistema e menu em console.
- `app/context`: composição das dependências da aplicação.
- `domain/model`: classes principais do domínio.
- `service`: regras de negócio.
- `repository`: contratos de persistência.
- `repository/memory`: implementação em memória para evoluir sem depender do banco.
- `util`: leitura de entrada e formatação de saída.
- `docs/database`: base inicial do esquema relacional.
- `test`: testes unitários iniciais.

## Arquitetura adotada
A base foi separada em três responsabilidades principais:
- **Domínio**: representa as entidades e comportamentos centrais do sistema.
- **Serviços**: concentra as regras de negócio e os casos de uso.
- **Persistência**: isola o armazenamento, permitindo trocar memória por JDBC/MySQL depois sem reescrever o restante.

## Funcionalidades disponíveis
- cadastrar produtos;
- listar produtos;
- cadastrar clientes;
- listar clientes;
- registrar vendas;
- listar vendas realizadas;
- baixar estoque automaticamente ao adicionar item na venda.

## Tecnologias
- Java 17
- Maven
- JUnit 5
- MySQL (planejado para a próxima fase)
- JDBC (planejado para a próxima fase)

## Como executar
### Pelo IntelliJ
Abra o projeto Maven e rode a classe `br.edu.felipebueno.arcade.app.Main`.

### Pelo terminal

```bash
mvn compile
mvn exec:java -Dexec.mainClass="br.edu.felipebueno.arcade.app.Main"
```

## Como rodar os testes
### Pelo terminal
```bash
mvn test
```

## Próximos passos sugeridos
1. Criar persistência JDBC para produto, cliente e venda.
2. Persistir categorias em uma estrutura própria.
3. Adicionar atualização e remoção no menu.
4. Expandir os testes de regras de negócio.
5. Refinar a interface com base nas próximas entregas da disciplina.
