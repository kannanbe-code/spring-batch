package com.example.batchrest.repository;

import com.example.batchrest.model.FailedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedCustomerRepository extends JpaRepository<FailedCustomer, Long> {
}
