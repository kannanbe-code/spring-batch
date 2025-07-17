package com.example.batchrest.email;

import com.example.batchrest.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendFailureEmail(Customer customer, Exception e) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("alert@example.com");
        message.setSubject("Customer Sync Failure");
        message.setText("Failed to send customer: " + customer.getId() + ", " + customer.getName()
                + "\nError: " + e.getMessage());

        mailSender.send(message);
    }
}
