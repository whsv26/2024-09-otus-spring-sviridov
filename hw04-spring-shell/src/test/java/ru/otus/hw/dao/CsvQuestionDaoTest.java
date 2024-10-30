package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {CsvQuestionDao.class})
public class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Should fail for non existing resource")
    @Test
    void shouldFailForNonExistingResource() {
        given(fileNameProvider.getTestFileName()).willReturn("nowhere.csv");
        assertThatExceptionOfType(QuestionReadException.class)
            .isThrownBy(() -> csvQuestionDao.findAll());
    }

    @DisplayName("Should parse questions")
    @Test
    void shouldParseQuestions() {
        given(fileNameProvider.getTestFileName()).willReturn("questions.csv");
        var actual = csvQuestionDao.findAll();
        var expected = List.of(
            new Question("q1", List.of(
                new Answer("a1", true),
                new Answer("a2", false),
                new Answer("a3", false)
            )),
            new Question("q2", List.of(
                new Answer("a1", false),
                new Answer("a2", true),
                new Answer("a3", false)
            )),
            new Question("q3", List.of(
                new Answer("a1", false),
                new Answer("a2", false),
                new Answer("a3", true)
            )),
            new Question("q4", List.of(
                new Answer("a1", true),
                new Answer("a2", false)
            )),
            new Question("q5", List.of(
                new Answer("a1", true)
            ))
        );
        assertThat(actual).isEqualTo(expected);
    }
}
