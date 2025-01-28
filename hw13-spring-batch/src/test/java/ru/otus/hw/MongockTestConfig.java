package ru.otus.hw;

import com.mongodb.WriteConcern;
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
@EnableMongock
public class MongockTestConfig {

    @Primary
    @Bean
    public SpringDataMongoV4Driver connectionDriverWithoutJournal(MongoTemplate mongoTemplate) {
        var driver = SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
        driver.disableTransaction();
        driver.setWriteConcern(WriteConcern.W1.withJournal(false));
        return driver;
    }
}
