Implement Authorization Based on URL and/or Domain Entities

## Goal

Learn to secure an application with proper authorization and access control

## Result

A fully secured application using Spring Security

## Description

The task is based on a non-reactive Spring MVC application

- Minimum: configure URL-based authorization in the application.
- Maximum: configure authorization based on domain entities and service methods.

Recommendations:
- It's not recommended to separate users with different roles into different classes – use a single user class.
- When implementing domain entity-based authorization with PostgreSQL, do not use GUIDs for entities.
- Write controller tests to verify that all necessary resources are indeed protected.
