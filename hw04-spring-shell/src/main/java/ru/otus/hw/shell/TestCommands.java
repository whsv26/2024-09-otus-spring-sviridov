package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.security.StudentContext;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent("Test application commands")
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    private final StudentContext studentContext;

    @ShellMethod(value = "Sign in as a student", key = {"login", "sign-in", "l"})
    public void login() {
        studentContext.login();
    }

    @ShellMethod(value = "Start the test", key = {"test", "t"})
    public void test() {
        testRunnerService.run();
    }

    public Availability testAvailability() {
        return studentContext.isLoggedIn()
            ? Availability.available()
            : Availability.unavailable("you are not logged in");
    }
}
