package ru.otus.hw.changelogs;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ChangeUnit(id = "dropDbChangeUnitId", order = "000", runAlways = true)
public class DropDbChangeUnit {

    private final MongoDatabase database;

    @Execution
    public void dropDatabase() {
        database.drop();
    }

    @RollbackExecution
    public void rollback() {

    }
}
