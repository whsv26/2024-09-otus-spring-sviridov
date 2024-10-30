package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.StudentContext;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentContext studentContext;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = studentContext.getStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
