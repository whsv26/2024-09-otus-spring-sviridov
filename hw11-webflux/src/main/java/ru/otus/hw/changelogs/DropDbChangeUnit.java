package ru.otus.hw.changelogs;

import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@ChangeUnit(id = "dropDbChangeUnitId", order = "000", runAlways = true)
public class DropDbChangeUnit {

    private final MongoDatabase database;

    @Execution
    public void dropDatabase() {
        Flux.from(database.drop()).blockLast();
    }

    @RollbackExecution
    public void rollback() {

    }
}
