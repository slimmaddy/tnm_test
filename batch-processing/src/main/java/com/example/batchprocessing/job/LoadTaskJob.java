package com.example.batchprocessing.job;

import com.example.batchprocessing.JobCompletionNotificationListener;
import com.example.batchprocessing.model.Task;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class LoadTaskJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public Step deleteFile;

    @Bean
    @StepScope
    public FlatFileItemReader<Task> taskReader(@Value("#{jobParameters[file_path]}") String filePath) {
        final FileSystemResource fileResource = new FileSystemResource(filePath);

        return new FlatFileItemReaderBuilder<Task>()
                .name("taskItemReader")
                .resource(fileResource)
                .linesToSkip(1)
                .delimited()
                .names("taskId", "skill")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Task>() {{
                    setTargetType(Task.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Task> taskWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Task>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO task (task_id, skill) VALUES (:taskId, :skill)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTaskJob(JobCompletionNotificationListener listener, Step importTaskStep) {
        return jobBuilderFactory.get("importTaskJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importTaskStep)
                .next(deleteFile)
                .end()
                .build();
    }

    @Bean
    public Step importTaskStep(JdbcBatchItemWriter<Task> taskWriter, ItemReader<Task> taskReader) {
        return stepBuilderFactory.get("importTaskStep")
                .<Task, Task> chunk(10)
                .reader(taskReader)
                .writer(taskWriter)
                .build();
    }
}
