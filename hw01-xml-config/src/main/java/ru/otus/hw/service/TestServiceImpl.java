package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionsDao;

    @Override
    public void executeTest() {
        var header = writeHeader();
        var questions = writeQuestions(questionsDao.findAll());
        Stream.concat(header, questions).forEach(ioService::printLine);
    }

    private static Stream<String> writeHeader() {
        return Stream.of("", "Please answer the questions below", "");
    }

    private static Stream<String> writeQuestions(List<Question> questions) {
        Stream<String> lines = Stream.empty();
        int nth = 1;

        for (var question : questions) {
            lines = Stream.concat(lines, writeQuestion(question, nth++));
            lines = Stream.concat(lines, Stream.of(""));
        }

        return lines;
    }

    private static Stream<String> writeQuestion(Question question, int nth) {
        var questionText = Stream.of("%d) %s".formatted(nth, question.text()));
        var questionAnswers = writeAnswers(question.answers());
        return Stream.concat(questionText, questionAnswers);
    }

    private static Stream<String> writeAnswers(List<Answer> answers) {
        return answers.stream()
            .map(Answer::text)
            .map("- %s"::formatted);
    }

}
