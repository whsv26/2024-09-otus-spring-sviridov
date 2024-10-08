package ru.otus.hw;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("spring-context.xml")) {
            context.getBean(TestRunnerService.class).run();
        }
    }
}