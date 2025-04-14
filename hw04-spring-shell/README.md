Migrate the survey application to Spring Shell

## Goal

After completing this assignment, you will be able to use Spring Shell to build application interfaces without using Web.

## Result

Application using Spring Shell

## Description

- Integrate Spring Shell using the spring-starter
- Create a set of commands that allow conducting the survey
- Migrate tests to @SpringBootTest
  - Components and mocks should be taken from the test context
  - These should otherwise be unit tests
  - Keep in mind that Spring Shell should be disabled during tests
- The set of commands is entirely up to you
  - You can implement a single command to start the survey
  - Or build a full-featured interface using Spring Shell
- Localization of Spring Shell commands is not required (though possible, it is lengthy and complex)
- This assignment does not require using Spring Application Events, especially not for implementing business logic
