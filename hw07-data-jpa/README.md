Rewrite the application for book storage using Spring Data JPA

## Goal

Simplify writing the repository layer using modern approaches

## Result

Application with a repository layer based on Spring Data JPA

## Description

- Rewrite all repositories related to book management using Spring Data JPA repositories
- Use spring-boot-starter-data-jpa
- Cover custom repository methods (or those with complex @Query) with tests using H2
- It is recommended to place @Transactional on service methods rather than repository methods