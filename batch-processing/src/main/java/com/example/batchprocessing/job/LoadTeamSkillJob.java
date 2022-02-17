package com.example.batchprocessing.job;

import com.example.batchprocessing.JobCompletionNotificationListener;
import com.example.batchprocessing.model.TeamSkill;
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
public class LoadTeamSkillJob {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public Step deleteFile;

    @Bean
    @StepScope
    public FlatFileItemReader<TeamSkill> teamSkillReader(@Value("#{jobParameters[file_path]}") String filePath) {
        final FileSystemResource fileResource = new FileSystemResource(filePath);

        return new FlatFileItemReaderBuilder<TeamSkill>()
                .name("teamSkillItemReader")
                .resource(fileResource)
                .linesToSkip(1)
                .delimited()
                .names("teamId", "skill")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TeamSkill>() {{
                    setTargetType(TeamSkill.class);
                }})
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<TeamSkill> teamSkillWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TeamSkill>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO team_skill (team_id, skill) VALUES (:teamId, :skill)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTeamSkillJob(JobCompletionNotificationListener listener, Step importTeamSkillStep) {
        return jobBuilderFactory.get("importTeamSkillJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importTeamSkillStep)
                .next(deleteFile)
                .end()
                .build();
    }

    @Bean
    public Step importTeamSkillStep(JdbcBatchItemWriter<TeamSkill> teamSkillWriter, ItemReader<TeamSkill> teamSkillReader) {
        return stepBuilderFactory.get("importTeamSkillStep")
                .<TeamSkill, TeamSkill> chunk(10)
                .reader(teamSkillReader)
                .writer(teamSkillWriter)
                .build();
    }
}
