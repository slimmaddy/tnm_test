package com.example.batchprocessing.job;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.Assert;

import java.io.File;

public class FileDeleteTasklet implements Tasklet, InitializingBean {

    private FileSystemResource resource;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        File file = resource.getFile();
        boolean deleted = file.delete();
        if (!deleted) {
            throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
        }
        return RepeatStatus.FINISHED;
    }

    public void setResource(FileSystemResource resource) {
        this.resource = resource;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resource, "file path must be set");
    }
}
