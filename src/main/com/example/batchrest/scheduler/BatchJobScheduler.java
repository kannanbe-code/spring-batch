package com.example.batchrest.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.time.LocalDateTime;

@Component
public class BatchJobScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job customerJob;

    @Scheduled(fixedDelay = 60000) // every 60 sec
    public void runJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

            jobLauncher.run(customerJob, params);
            System.out.println("Batch job started at " + LocalDateTime.now());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
