package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job exportFromMongoToH2Job;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Export from MongoDB to H2", key = {"export-from-mongo-to-h2-job", "export"})
    public void exportToH2() {
        JobExecution execution = null;
        try {
            execution = jobLauncher.run(
                exportFromMongoToH2Job,
                new JobParametersBuilder().toJobParameters()
            );
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
        System.out.println(execution);
    }
}
