package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            var answerTexts = question.answers().stream().map(Answer::text).toList();
            var selectedAnswerText = ioService.selectStringWithPrompt(question.text(), answerTexts);
            var selectedAnswer = findAnswerByText(question, selectedAnswerText);

            testResult.applyAnswer(question, selectedAnswer.isCorrect());
        }

        return testResult;
    }

    private static Answer findAnswerByText(Question question, String text) {
        return question.answers().stream()
            .filter(answer -> answer.text().equals(text))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Unknown answer"));
    }
}
