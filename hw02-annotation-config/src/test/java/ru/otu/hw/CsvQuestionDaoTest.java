package ru.otu.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
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
                new Answer("a3", true)
            )),
            new Question("q2", List.of(
                new Answer("a1", false),
                new Answer("a2", true),
                new Answer("a3", false)
            )),
            new Question("q3", List.of(
                new Answer("a1", true),
                new Answer("a2", true),
                new Answer("a3", true)
            )),
            new Question("q4", List.of(
                new Answer("a1", true),
                new Answer("a2", true)
            )),
            new Question("q5", List.of(
                new Answer("a1", false)
            ))
        );
        assertThat(actual).isEqualTo(expected);
    }
}
