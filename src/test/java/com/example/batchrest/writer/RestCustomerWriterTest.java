package com.example.batchrest.writer;

import com.example.batchrest.model.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

public class RestCustomerWriterTest {

    @Test
    void testWrite_withSuccess() {
        WebClient.Builder mockBuilder = Mockito.mock(WebClient.Builder.class, Mockito.RETURNS_DEEP_STUBS);
        WebClient webClient = WebClient.builder().build();
        RetryTemplate retryTemplate = new RetryTemplate();

        // Mock WebClient behavior here if needed (simplified)
        RestCustomerWriter writer = new RestCustomerWriter(WebClient.builder(), retryTemplate);

        Customer customer = new Customer();
        customer.setName("Retry Test");
        customer.setEmail("retry@example.com");

        assertDoesNotThrow(() -> writer.write(List.of(customer)));
    }
}
