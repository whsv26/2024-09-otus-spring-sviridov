Rewrite the Book Storage Application Using ORM

## Goal

Work effectively with JPA + Hibernate to connect to relational databases through an ORM framework

## Result

A high-level application with JPA entity mapping

## Description

This homework involves rewriting the previous assignment using JPA.

- Use JPA with Hibernate only as the JPA provider
- Spring Data is not allowed at this stage
- Entity relationship loading should avoid excessive database queries or unnecessarily large data sets (avoid N+1 problem and cartesian product issue)
- Add an entity "book comment" and implement CRUD for it. You do not need to retrieve all comments — only a specific comment by its ID and all comments for a specific book by its ID
- DDL generation via Hibernate should be disabled
- LAZY relationships should not be included in equals/hashCode/toString methods. This also applies when using @Data
- The @Transactional annotation should only be present on service methods
- Cover repositories with tests using an H2 database and @DataJpaTest
- Write integration tests for book and comment services to verify database operations.
  - Transactions in these tests should be disabled so they don’t affect service transactions
  - Ensure that access to relationships used outside of services does not trigger LazyInitializationException
  - Don’t forget to consider context caching in tests
- Include tests from the [template](https://github.com/OtusTeam/Spring/tree/master/templates/hw06-jpa). All tests must pass for the assignment to be accepted
