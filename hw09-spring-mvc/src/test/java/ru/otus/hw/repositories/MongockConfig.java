package ru.otus.hw.repositories;

import io.mongock.driver.mongodb.springdata.v4.config.SpringDataMongoV4Context;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableMongock
@Import({SpringDataMongoV4Context.class})
public class MongockConfig {

}
