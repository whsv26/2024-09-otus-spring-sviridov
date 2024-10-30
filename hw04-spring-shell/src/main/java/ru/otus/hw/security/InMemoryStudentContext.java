package ru.otus.hw.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InMemoryStudentContext implements StudentContext {

    @Getter
    private Student student;

    private final LocalizedIOService ioService;

    @Override
    public void login() {
        var firstName = ioService.readStringWithPromptLocalized("StudentService.input.first.name");
        var lastName = ioService.readStringWithPromptLocalized("StudentService.input.last.name");
        student = new Student(firstName, lastName);
    }

    @Override
    public boolean isLoggedIn() {
        return Objects.nonNull(student);
    }
}
