package com.example.batchprocessing;

import com.example.batchprocessing.producer.MessageSender;
import com.example.batchprocessing.producer.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final MessageSender messageSender;

    @Autowired
    public JobCompletionNotificationListener(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            messageSender.send(new Notification(System.currentTimeMillis()));
        }
    }
}
