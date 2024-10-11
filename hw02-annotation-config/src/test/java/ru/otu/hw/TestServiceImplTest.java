package ru.otu.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestServiceImplTest {

    public static final Student STUDENT = new Student("Alexander", "Sviridov");

    private SimpleIOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        ioService = new SimpleIOService();
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @DisplayName("Should print questions with answers")
    @Test
    public void shouldPrintQuestionsWithAnswers() {
        questionsShouldBe(
            new Question("q1", List.of(
                new Answer("q1.a1", true),
                new Answer("q1.a2", true)
            )),
            new Question("q2", List.of(
                new Answer("q2.a1", true),
                new Answer("q2.a2", true),
                new Answer("q2.a3", true)
            ))
        );
        answersShouldBe("1", "1");

        testService.executeTestFor(STUDENT);

        var actual = ioService.getWriteSink();
        var expected = List.of(
            "Please answer the questions below",
            "q1",
            "- 1) q1.a1",
            "- 2) q1.a2",
            "Enter correct answer numbers from 1 to 2, separated by commas",
            "q2",
            "- 1) q2.a1",
            "- 2) q2.a2",
            "- 3) q2.a3",
            "Enter correct answer numbers from 1 to 3, separated by commas"
        );

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Should retry invalid read")
    @Test
    public void shouldRetryInvalidRead() {
        var question = new Question(
            "q",
            List.of(new Answer("a", true))
        );
        var answers = new String[]{"2", "1"};

        questionsShouldBe(question);
        answersShouldBe(answers);

        testService.executeTestFor(STUDENT);

        var actual = ioService.getWriteSink();
        var expected = List.of("Please answer the questions below",
            "q",
            "- 1) a",
            "Enter correct answer numbers from 1 to 1, separated by commas",
            "Invalid answer format, try again",
            "Enter correct answer numbers from 1 to 1, separated by commas"
        );

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Should accept one combination for single answer question")
    @Test
    public void shouldAcceptOneCombinationForSingleAnswerQuestion() {
        var question = new Question("q", List.of(
            new Answer("a1", false),
            new Answer("a2", true),
            new Answer("a3", false)
        ));
        var answers = new String[]{"1", "2", "3", "1,2", "1,3", "2,3", "1,2,3"};
        var questions = Collections.nCopies(answers.length, question);

        questionsShouldBe(questions.toArray(Question[]::new));
        answersShouldBe(answers);

        var testResult = testService.executeTestFor(STUDENT);
        var rightAnswersCount = testResult.getRightAnswersCount();

        assertThat(rightAnswersCount).isEqualTo(1);
    }

    @DisplayName("Should accept one combination for muti answer question")
    @Test
    public void shouldAcceptOneCombinationForMutiAnswerQuestion() {
        var question = new Question("q", List.of(
            new Answer("a1", true),
            new Answer("a2", false),
            new Answer("a3", true)
        ));
        var answers = new String[]{"1", "2", "3", "1,2", "1,3", "2,3", "1,2,3"};
        var questions = Collections.nCopies(answers.length, question);

        questionsShouldBe(questions.toArray(Question[]::new));
        answersShouldBe(answers);

        var testResult = testService.executeTestFor(STUDENT);
        var rightAnswersCount = testResult.getRightAnswersCount();

        assertThat(rightAnswersCount).isEqualTo(1);
    }

    private void questionsShouldBe(Question... questions) {
        given(questionDao.findAll()).willReturn(Arrays.asList(questions));
    }

    private void answersShouldBe(String... answers) {
        var answerSource = Arrays.stream(answers).iterator();
        ioService.setReadSource(answerSource);
    }

}
