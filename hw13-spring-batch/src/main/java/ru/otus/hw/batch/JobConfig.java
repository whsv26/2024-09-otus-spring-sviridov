package ru.otus.hw.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class JobConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Job exportFromMongoToH2Job(
        JobRepository jobRepository,
        Flow authorAndGenreFlow,
        Step bookStep,
        Step commentStep
    ) {
        return new JobBuilder("exportFromMongoToH2Job", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(authorAndGenreFlow)
            .next(bookStep)
            .next(commentStep)
            .end()
            .build();
    }

    @Bean
    public Flow authorAndGenreFlow(Flow authorFlow, Flow genreFlow) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
            .split(new SimpleAsyncTaskExecutor())
            .add(authorFlow, genreFlow)
            .build();
    }

    @Bean
    public Flow authorFlow(Step authorStep) {
        return new FlowBuilder<SimpleFlow>("authorFlow")
            .start(authorStep)
            .build();
    }

    @Bean
    public Flow genreFlow(Step genreStep) {
        return new FlowBuilder<SimpleFlow>("genreFlow")
            .start(genreStep)
            .build();
    }

    @StepScope
    @Bean
    ItemReader<Author> authorReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Author>()
            .name("authorReader")
            .template(mongoTemplate)
            .collection("author")
            .jsonQuery("{}")
            .targetType(Author.class)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public Step authorStep(
        ItemReader<Author> reader,
        ItemWriter<Author> writer,
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("authorStep", jobRepository)
            .<Author, Author>chunk(10, transactionManager)
            .reader(reader)
            .processor(item -> item)
            .writer(writer)
            .build();
    }

    @StepScope
    @Bean
    ItemReader<Genre> genreReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Genre>()
            .name("genreReader")
            .template(mongoTemplate)
            .collection("genre")
            .jsonQuery("{}")
            .targetType(Genre.class)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public Step genreStep(
        ItemReader<Genre> reader,
        ItemWriter<Genre> writer,
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("genreStep", jobRepository)
            .<Genre, Genre>chunk(10, transactionManager)
            .reader(reader)
            .processor(item -> item)
            .writer(writer)
            .build();
    }

    @StepScope
    @Bean
    ItemReader<Book> bookReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Book>()
            .name("bookReader")
            .template(mongoTemplate)
            .collection("book")
            .jsonQuery("{}")
            .targetType(Book.class)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public Step bookStep(
        ItemReader<Book> reader,
        ItemWriter<Book> writer,
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("bookStep", jobRepository)
            .<Book, Book>chunk(10, transactionManager)
            .reader(reader)
            .processor(item -> item)
            .writer(writer)
            .build();
    }

    @StepScope
    @Bean
    ItemReader<Comment> commentReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Comment>()
            .name("commentReader")
            .template(mongoTemplate)
            .collection("comment")
            .jsonQuery("{}")
            .targetType(Comment.class)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public Step commentStep(
        ItemReader<Comment> reader,
        ItemWriter<Comment> writer,
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("commentStep", jobRepository)
            .<Comment, Comment>chunk(10, transactionManager)
            .reader(reader)
            .processor(item -> item)
            .writer(writer)
            .build();
    }
}
