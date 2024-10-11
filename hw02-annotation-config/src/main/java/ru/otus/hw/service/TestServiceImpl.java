package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private static final char SEPARATOR = ',';

    private static final int MAX_ATTEMPTS = 10;

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");
        ioService.printLine("");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        questions.forEach(question -> askQuestion(question, testResult));

        return testResult;
    }

    private void askQuestion(Question question, TestResult testResult) {
        printQuestion(question);
        var pickedAnswers = readQuestionAnswers(question);
        var isAnswerValid = question.isAnsweredCorrectly(pickedAnswers);
        testResult.applyAnswer(question, isAnswerValid);
    }

    private Set<Answer> readQuestionAnswers(Question question) {
        var min = 1;
        var max = question.answers().size();
        var retryMessage = "Invalid answer format, try again";
        var readPrompt = "Enter correct answer numbers from %d to %d, separated by commas".formatted(min, max);
        var intsReader = ListReader.create(new BoundedIntReader(min, max), SEPARATOR);

        return retry(
            () -> ioService.printLine(retryMessage),
            () -> ioService.readWithPrompt(intsReader, readPrompt)
                .stream()
                .map(n -> question.answers().get(n - 1))
                .collect(Collectors.toSet())
        );
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        var nth = 1;
        for (var answer : question.answers()) {
            ioService.printFormattedLine("- %s) %s", nth++, answer.text());
        }
    }

    private static <T> T retry(Runnable onError, Supplier<T> action) {
        var attempt = 0;
        while (true) {
            try {
                return action.get();
            } catch (Exception e) {
                onError.run();
                if (attempt >= MAX_ATTEMPTS) {
                    throw e;
                }
                attempt++;
            }
        }
    }
}
