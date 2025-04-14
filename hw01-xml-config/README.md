Student Testing Application – Only Displaying Questions and Answer Options (if available)

## Goal

Create an application using Spring IoC to get acquainted with the core functionality of IoC, which is the foundation of all Spring applications.

## Result

A simple application configured with an XML context.

## Description

- The resources contain questions and their possible answers in a CSV file (5 questions).
- Questions can be multiple choice or open-ended — as you prefer.
- The application should simply display the test questions from the CSV file along with any available answer options.

## Requirements

- The application should be based on the [homework template](https://github.com/OtusTeam/Spring/tree/master/templates/hw01-xml-config).
- The application must include an object model (favor objects and classes over strings and arrays/lists of strings).
- All classes in the application must serve a specific, clearly defined purpose (see points 18–19 in the "Code Style Guidelines.pdf" attached to the course materials).
- The context should be defined in an XML file.
- All dependencies must be configured in the IoC container.
- The name of the resource file (CSV) with the questions must be hardcoded as a string in the XML context file.
- The CSV file must be read as a resource, not as a file.
- Do not place Scanner, PrintStream, or other standard types into the application context!
- All input/output should be in English.
- Write a unit test for one of the services.
- The application should correctly launch with "java -jar" (Optional)
