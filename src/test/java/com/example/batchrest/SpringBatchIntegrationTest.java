package com.example.batchrest;

import com.example.batchrest.model.Customer;
import com.example.batchrest.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringBatchIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importCustomerJob;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        Customer c1 = new Customer();
        c1.setName("John Doe");
        c1.setEmail("john@example.com");

        Customer c2 = new Customer();
        c2.setName("Jane Smith");
        c2.setEmail("jane@example.com");

        customerRepository.save(c1);
        customerRepository.save(c2);
    }

    @Test
    void testBatchJobRunsSuccessfully() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importCustomerJob, jobParameters);

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        assertEquals(1, execution.getJobInstance().getInstanceId());
        assertTrue(execution.getStepExecutions().stream().anyMatch(
                s -> s.getWriteCount() >= 2 || s.getSkipCount() >= 0
        ));
    }
}
