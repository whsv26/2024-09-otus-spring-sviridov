package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

public class TestServiceImplTest {

    private InMemoryIOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        ioService = new InMemoryIOService();
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void shouldPrintTheHeader() {
        given(questionDao.findAll()).willReturn(List.of());

        testService.executeTest();

        var actual = ioService.getLinesLog();
        var expected = List.of("", "Please answer the questions below", "");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrintMultipleQuestions() {
        var question1 = new Question("q1", List.of(
            new Answer("q1.a1", true),
            new Answer("q1.a2", false)
        ));
        var question2 = new Question("q2", List.of(
            new Answer("q2.a1", true),
            new Answer("q2.a2", false)
        ));

        given(questionDao.findAll()).willReturn(List.of(question1, question2));

        testService.executeTest();

        var lines = ioService.getLinesLog();
        var actual = lines.subList(3, lines.size());
        var expected = List.of(
            "1) q1", "- q1.a1", "- q1.a2", "",
            "2) q2", "- q2.a1", "- q2.a2", ""
        );

        assertThat(actual).isEqualTo(expected);
    }

}
