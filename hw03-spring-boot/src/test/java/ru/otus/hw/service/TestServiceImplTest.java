package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    public static final Student STUDENT = new Student("Alexander", "Sviridov");

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @DisplayName("Should print questions with answers")
    @Test
    public void shouldPrintQuestionsWithAnswers() {
        answersShouldBe(1, 1);
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

        testService.executeTestFor(STUDENT);

        var inOrder = inOrder(ioService);

        List.of(
            "q1",
            "- 1) q1.a1",
            "- 2) q1.a2",
            "q2",
            "- 1) q2.a1",
            "- 2) q2.a2",
            "- 3) q2.a3"
        ).forEach(s -> inOrder.verify(ioService).printLine(eq(s)));
    }

    @DisplayName("Should accept one right answer")
    @Test
    public void shouldAcceptOneRightAnswer() {
        var question = new Question("q", List.of(
            new Answer("a1", false),
            new Answer("a2", true),
            new Answer("a3", false)
        ));
        var answers = new int[]{1, 2, 3};
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

    private void answersShouldBe(int... answers) {
        var stub = given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), anyString()));
        for (var answer : answers) {
            stub = stub.willReturn(answer);
        }
    }
}
