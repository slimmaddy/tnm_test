package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.util.Objects;

@Configuration
public class IntegrationConfiguration {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Job importTaskJob;

    @Autowired
    private Job importTeamJob;

    @Autowired
    private Job importTeamSkillJob;

    @Bean
    public IntegrationFlow sampleFlow() {
        return IntegrationFlows
                .from(fileReadingMessageSource(), c -> c.poller(Pollers.fixedDelay(1000)
                        .maxMessagesPerPoll(10)))
                .transform(Message.class, this::transform)
                .filter(Objects::nonNull)
                .handle(jobLaunchingGateway())
                .log(LoggingHandler.Level.INFO, "headers.id + ': ' + payload")
                .get();
    }

    public JobLaunchRequest transform(Message<File> message) {
        JobParametersBuilder jobParametersBuilder =
                new JobParametersBuilder();

        JobParameters jobParameter = jobParametersBuilder
                .addString("file_path", message.getPayload().getAbsolutePath())
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        String fileName = message.getPayload().getAbsoluteFile().getName();
        if (fileName.contains("task.csv")) {
            return new JobLaunchRequest(importTaskJob, jobParameter);
        } else if (fileName.contains("team.csv")) {
            return new JobLaunchRequest(importTeamJob, jobParameter);
        } else {
            return new JobLaunchRequest(importTeamSkillJob, jobParameter);
        }
    }

    @Bean
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("/data"));
        source.setFilter(new RegexPatternFileListFilter("(task|team|team_skill)\\.csv"));
        source.setUseWatchService(true);
        source.setWatchEvents(FileReadingMessageSource.WatchEventType.CREATE);
        return source;
    }

    @Bean
    JobLaunchingGateway jobLaunchingGateway() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(taskExecutor());

        return new JobLaunchingGateway(simpleJobLauncher);
    }

    @Bean
    ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.afterPropertiesSet();

        return taskExecutor;
    }
}
