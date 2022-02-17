package com.example.batchprocessing.job;

import com.example.batchprocessing.JobCompletionNotificationListener;
import com.example.batchprocessing.model.Team;
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
public class LoadTeamJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public Step deleteFile;

    @Bean
    @StepScope
    public FlatFileItemReader<Team> teamReader(@Value("#{jobParameters[file_path]}") String filePath) {
        final FileSystemResource fileResource = new FileSystemResource(filePath);

        return new FlatFileItemReaderBuilder<Team>()
                .name("teamItemReader")
                .resource(fileResource)
                .linesToSkip(1)
                .delimited()
                .names(new String[]{"teamId"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Team>() {{
                    setTargetType(Team.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Team> teamWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Team>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO team (team_id) VALUES (:teamId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTeamJob(JobCompletionNotificationListener listener, Step importTeamStep) {
        return jobBuilderFactory.get("importTeamJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importTeamStep)
                .next(deleteFile)
                .end()
                .build();
    }

    @Bean
    public Step importTeamStep(JdbcBatchItemWriter<Team> teamWriter, ItemReader<Team> teamReader) {
        return stepBuilderFactory.get("importTeamStep")
                .<Team, Team> chunk(10)
                .reader(teamReader)
                .writer(teamWriter)
                .build();
    }
}