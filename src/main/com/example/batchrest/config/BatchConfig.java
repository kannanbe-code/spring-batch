package com.example.batchrest.config;

import com.example.batchrest.listener.JobAuditListener;
import com.example.batchrest.model.Customer;
import com.example.batchrest.repository.CustomerRepository;
import com.example.batchrest.writer.CustomerItemWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.support.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job customerJob(JobBuilderFactory jobBuilderFactory,
                           StepBuilderFactory stepBuilderFactory,
                           RepositoryItemReader<Customer> reader,
                           CustomerItemWriter writer,
                           JobAuditListener listener) {
        Step step = stepBuilderFactory.get("customerStep")
            .<Customer, Customer>chunk(10)
            .reader(reader)
            .writer(writer)
            .listener(listener)
            .build();

        return jobBuilderFactory.get("customerJob")
            .incrementer(new RunIdIncrementer())
            .start(step)
            .build();
    }

    @Bean
    public RepositoryItemReader<Customer> reader(CustomerRepository repo) {
        return new RepositoryItemReaderBuilder<Customer>()
            .name("customerReader")
            .repository(repo)
            .methodName("findAll")
            .pageSize(10)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retry = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);

        FixedBackOffPolicy backOff = new FixedBackOffPolicy();
        backOff.setBackOffPeriod(2000); // 2s

        retry.setRetryPolicy(policy);
        retry.setBackOffPolicy(backOff);
        return retry;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
