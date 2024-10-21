package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            askQuestion(question);
            var selectedAnswer = selectAnswer(question);
            testResult.applyAnswer(question, selectedAnswer.isCorrect());
        }

        return testResult;
    }

    private Answer selectAnswer(Question question) {
        var min = 1;
        var max = question.answers().size();
        var retryMessage = "Invalid answer format, try again";
        var readPrompt = "Enter correct answer number from %d to %d".formatted(min, max);
        var answerOrdinal = ioService.readIntForRangeWithPrompt(min, max, readPrompt, retryMessage);

        return question.answers().get(answerOrdinal - 1);
    }

    private void askQuestion(Question question) {
        ioService.printLine(question.text());
        var answerOrdinal = 1;
        for (var answer : question.answers()) {
            ioService.printFormattedLine("- %s) %s", answerOrdinal++, answer.text());
        }
    }
}
