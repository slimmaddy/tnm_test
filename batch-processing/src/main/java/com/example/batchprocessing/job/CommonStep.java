package com.example.batchprocessing.job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class CommonStep {
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public Step deleteFile(@Value("#{jobParameters[file_path]}") String filePath) {
        final FileSystemResource fileResource = new FileSystemResource(filePath);

        FileDeleteTasklet task = new FileDeleteTasklet();
        task.setResource(fileResource);
        return stepBuilderFactory.get("deleteFileStep")
                .tasklet(task)
                .build();
    }
}
