Migrate the Student Testing Application to Spring Boot

## Goal

Use Spring Boot features to develop modern applications the way they are developed today.

## Result

A production-ready application based on Spring Boot

## Description

- The application should be based on the [homework template](https://github.com/OtusTeam/Spring/tree/master/templates/hw03-spring-boot).
- Create the project using [Spring Boot Initializr](https://start.spring.io)
- Migrate the student survey application from the previous homework
- Move all properties into `application.yml`
- Localize output messages and questions (from the CSV file)
  - `MessageSource` should come from Spring Boot autoconfiguration
- Create a custom banner for the application
- Instead of individual test dependencies in the build file, use `spring-boot-starter-test`
- Use ANSI colors for the banner (optional)
- If your language differs from Russian or English – localize in your language (optional)
