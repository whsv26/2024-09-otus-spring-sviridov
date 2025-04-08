package me.whsv26.novel.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.whsv26.libs.outbox.mongo.OutboxChangeStreamWatcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunner implements CommandLineRunner {

    private final OutboxChangeStreamWatcher outboxChangeStreamWatcher;

    @Override
    public void run(String... args) {
        outboxChangeStreamWatcher.watchForChanges();
    }
}
