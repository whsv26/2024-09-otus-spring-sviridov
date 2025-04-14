Student Testing Application (including the actual testing functionality)

## Goal

Configure Spring applications in a modern way, as it is done in today's development practices

## Result

A ready-to-use modern application on pure Spring

## Description

- The resources contain questions and various possible answers in a CSV file (5 questions).
- The questions can be multiple-choice or open-ended — at your discretion.
- The program should ask the user for their first and last name, present 5 questions from the CSV file, and display the test results.
- This is based on the previous homework assignment, with the testing functionality added.

## Requirements

- The application should be based on the [homework template](https://github.com/OtusTeam/Spring/tree/master/templates/hw02-annotation-config).
- The application must include an object model (prefer objects and classes over strings and lists/arrays of strings).
- All classes must serve a single, clearly defined purpose (see points 18-19 in "Code Style Guidelines.pdf" attached to the course materials).
- Rewrite the configuration using Java + Annotation-based configuration. All dependencies must be set up in the IoC container.
- Add the student testing functionality.
- Add a configuration file for the student testing application.
- You may include in the config file the path to the CSV file and the number of correct answers required to pass — at your discretion.
- Remember, the CSV with questions should be read as a resource, not as a file.
- Write an integration test for the class that reads questions, and a unit test for the service using a mock dependency. Integration here means integration with the file system. Otherwise, it should be a unit test.
- The settings file and the question file must have separate versions for tests.
- Do not place Scanner, PrintStream, and other standard types into the application context! See the relevant lecture slides.
- All input/output should be in English.
