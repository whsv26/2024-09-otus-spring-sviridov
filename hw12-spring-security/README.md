Add Authentication Mechanism to a CRUD Web Application

## Goal

Secure the Web application with authentication and simple authorization

## Result

Application using Spring Security

## Description

The task is based on a non-reactive Spring MVC application.

- Add a new entity to the application – user.
  It is not required to implement methods for user creation – it's acceptable to add users only via database scripts.
- Add Form-based authentication to the existing CRUD application.
- Implement UserDetailsService manually.
- Authorization on all pages should be restricted to authenticated users only. The login form should be accessible to everyone.
- Write controller tests that verify all required resources are properly secured.
