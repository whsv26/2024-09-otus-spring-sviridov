package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {TestServiceImpl.class,})
public class TestServiceImplTest {
    public static final Student STUDENT = new Student("Alexander", "Sviridov");

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    @DisplayName("Should accept one right answer")
    @Test
    public void shouldAcceptOneRightAnswer() {
        var question = new Question("q", List.of(
            new Answer("a1", false),
            new Answer("a2", true),
            new Answer("a3", false)
        ));
        var answers = new String[]{"a1", "a2", "a3"};
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

    private void answersShouldBe(String... answerTexts) {
        var stub = given(ioService.selectStringWithPrompt(anyString(), anyList()));
        for (var answer : answerTexts) {
            stub = stub.willReturn(answer);
        }
    }
}
