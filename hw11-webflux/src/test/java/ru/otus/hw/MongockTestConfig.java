package ru.otus.hw;

import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@EnableMongock
@Import(MongoReactiveAutoConfiguration.class)
public class MongockTestConfig {

    @Primary
    @Bean
    public MongoReactiveDriver reactiveConnectionDriverWithoutJournal(
        MongoClient mongoClient,
        MongoProperties mongoProperties
    ) {
        var driver = MongoReactiveDriver.withDefaultLock(mongoClient, mongoProperties.getDatabase());
        driver.disableTransaction();
        driver.setWriteConcern(WriteConcern.W1.withJournal(false));
        return driver;
    }
}
