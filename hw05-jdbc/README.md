Create an application – a catalog of books in a library

## Goal

Use Spring JDBC and spring-boot-starter-jdbc to connect to relational databases

## Result

An application with data stored in a relational database, which will be further developed

## Description

This homework is NOT based on the previous one.

- Use Spring JDBC and the H2 relational database
- Define entities for authors, books, and genres. Each should be stored in its own table
- Relationships are expected to be many-to-one (a book has one author, but an author can have multiple books; same for books and genres)
- The interface should be implemented using Spring Shell (CRUD for books plus, at minimum, commands to list all authors and genres)
- Schema creation and initialization should be done using schema.sql + data.sql or a migration system (Liquibase/Flyway)
- Use @JdbcTest to write integration tests for all DAO methods for books (with an embedded database)

Additional requirements for this assignment:
- The N+1 query problem must be resolved
- Use NamedParameterJdbcTemplate
- Do not create abstract or generic DAOs
- Do not create abstract or generic entities
- Do not create bidirectional relationships (a book has an author, but an author should not contain books)
- Optional complexity: many-to-many relationships for a single entity

Template links:

- Basic version: https://github.com/OtusTeam/Spring/tree/master/templates/hw05-jdbc-simple
- Version with optional complexity: https://github.com/OtusTeam/Spring/tree/master/templates/hw05-jdbc-hard