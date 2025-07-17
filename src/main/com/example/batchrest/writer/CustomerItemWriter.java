package com.example.batchrest.writer;

import com.example.batchrest.model.Customer;
import com.example.batchrest.model.FailedCustomer;
import com.example.batchrest.email.EmailNotificationService;
import com.example.batchrest.repository.FailedCustomerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CustomerItemWriter implements ItemWriter<Customer> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private FailedCustomerRepository failedRepo;

    @Autowired
    private EmailNotificationService emailService;

    @Override
    public void write(List<? extends Customer> customers) {
        for (Customer customer : customers) {
            try {
                retryTemplate.execute(context -> {
                    webClientBuilder.build()
                        .post()
                        .uri("http://localhost:8081/api/customers")
                        .bodyValue(customer)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                    return null;
                });
            } catch (Exception e) {
                logToFile(customer, e);
                logToDatabase(customer, e);
                emailService.sendFailureEmail(customer, e);
            }
        }
    }

    private void logToFile(Customer customer, Exception e) {
        try (FileWriter writer = new FileWriter("src/main/resources/failed-records.log", true)) {
            writer.write(String.format(
                "%s | ID: %d | Name: %s | Error: %s%n",
                LocalDateTime.now(), customer.getId(), customer.getName(), e.getMessage()
            ));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void logToDatabase(Customer customer, Exception e) {
        FailedCustomer failed = new FailedCustomer();
        failed.setCustomerId(customer.getId());
        failed.setName(customer.getName());
        failed.setEmail(customer.getEmail());
        failed.setErrorMessage(e.getMessage());
        failedRepo.save(failed);
    }
}
