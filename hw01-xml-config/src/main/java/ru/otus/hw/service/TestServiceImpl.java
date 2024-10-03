package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionsDao;

    @Override
    public void executeTest() {
        var header = writeHeader();
        var questions = writeQuestions(questionsDao.findAll().toArray(Question[]::new));
        Stream.concat(header, questions).forEach(ioService::printLine);
    }

    private static Stream<String> writeHeader() {
        return Stream.of("", "Please answer the questions below", "");
    }

    private static Stream<String> writeQuestions(Question[] questions) {
        return IntStream.range(0, questions.length)
            .mapToObj(i -> writeQuestion(questions[i], i + 1))
            .flatMap(Function.identity());
    }

    private static Stream<String> writeQuestion(Question question, int nth) {
        var questionText = Stream.of("%d) %s".formatted(nth, question.text()));
        var questionAnswers = question.answers().stream()
            .map(Answer::text)
            .map("- %s"::formatted);

        return Stream.of(questionText, questionAnswers, Stream.of(""))
            .flatMap(Function.identity());
    }

}
