package ru.otus.hw.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import ru.otus.hw.domain.AuthorId;
import ru.otus.hw.domain.ChapterId;
import ru.otus.hw.domain.GenreId;
import ru.otus.hw.domain.NovelId;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions(){
        return new MongoCustomConversions(List.of(
            new FromValueObjectConverter(),
            new ToValueObjectConverter<>(AuthorId::new),
            new ToValueObjectConverter<>(GenreId::new),
            new ToValueObjectConverter<>(NovelId::new),
            new ToValueObjectConverter<>(ChapterId::new)
        ));
    }
}