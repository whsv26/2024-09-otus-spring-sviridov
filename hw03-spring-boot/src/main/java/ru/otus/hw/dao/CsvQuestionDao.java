package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {

    public static final char SEPARATOR = ';';

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return read(reader ->
            streamAll(reader)
                .map(QuestionDto::toDomainObject)
                .toList()
        );
    }

    private static Stream<QuestionDto> streamAll(Reader reader) {
        return new CsvToBeanBuilder<QuestionDto>(reader)
            .withType(QuestionDto.class)
            .withSeparator(SEPARATOR)
            .withSkipLines(1)
            .build()
            .stream();
    }

    private <T> T read(Function<Reader, T> readWithReader) {
        var fileName = fileNameProvider.getTestFileName();
        try (var inputStream = new ClassPathResource(fileName).getInputStream();
             var inputStreamReader = new InputStreamReader(inputStream);
             var bufferedReader = new BufferedReader(inputStreamReader)) {
            return readWithReader.apply(bufferedReader);
        } catch (IOException e) {
            var message = "Failed to read a questions resource: %s".formatted(fileName);
            throw new QuestionReadException(message, e);
        }
    }
}
