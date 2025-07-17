package com.example.batchrest.listener;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobCompletionNotificationListenerTest {

    @Test
    public void testBeforeAndAfterJob() {
        JobCompletionNotificationListener listener = new JobCompletionNotificationListener();

        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setStatus(BatchStatus.COMPLETED);

        listener.beforeJob(jobExecution);
        listener.afterJob(jobExecution);

        assertTrue(true); // Ensure methods run without error
    }
}
