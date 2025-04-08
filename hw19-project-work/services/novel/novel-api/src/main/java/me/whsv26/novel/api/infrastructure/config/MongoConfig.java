package me.whsv26.novel.api.infrastructure.config;

import me.whsv26.novel.api.domain.valueobject.AuthorId;
import me.whsv26.novel.api.domain.valueobject.ChapterId;
import me.whsv26.novel.api.domain.valueobject.GenreId;
import me.whsv26.novel.api.domain.valueobject.NovelId;
import me.whsv26.novel.api.infrastructure.converter.FromValueObjectConverter;
import me.whsv26.novel.api.infrastructure.converter.ToValueObjectConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
            new FromValueObjectConverter(),
            new ToValueObjectConverter<>(AuthorId::new),
            new ToValueObjectConverter<>(GenreId::new),
            new ToValueObjectConverter<>(NovelId::new),
            new ToValueObjectConverter<>(ChapterId::new)
        ));
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}