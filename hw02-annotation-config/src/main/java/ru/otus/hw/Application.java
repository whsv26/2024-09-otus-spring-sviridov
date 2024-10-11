package ru.otus.hw;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.service.TestRunnerService;

@ComponentScan
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(Application.class)) {
            context.getBean(TestRunnerService.class).run();
        }
    }
}