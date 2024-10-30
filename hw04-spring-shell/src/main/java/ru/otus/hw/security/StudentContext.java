package ru.otus.hw.security;

import ru.otus.hw.domain.Student;

public interface StudentContext {
    void login();

    Student getStudent();

    boolean isLoggedIn();
}
