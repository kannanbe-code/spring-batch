package com.example.batchrest;

import com.example.batchrest.model.Customer;
import com.example.batchrest.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void testSaveAndRetrieveCustomers() {
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        repository.save(customer);

        List<Customer> customers = repository.findAll();
        assertFalse(customers.isEmpty());
    }
}
