package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        var questionsReader = makeQuestionsReader(fileNameProvider.getTestFileName());
        return new CsvToBeanBuilder<QuestionDto>(questionsReader)
            .withType(QuestionDto.class)
            .withSeparator(';')
            .withSkipLines(1)
            .build()
            .stream()
            .map(QuestionDto::toDomainObject)
            .toList();
    }

    private static Reader makeQuestionsReader(String fileName) {
        try {
            var inputStream = new ClassPathResource(fileName).getInputStream();
            var inputStreamReader = new InputStreamReader(inputStream);
            return new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            var message = "Failed to read a questions resource: %s".formatted(fileName);
            throw new QuestionReadException(message, e);
        }
    }
}
