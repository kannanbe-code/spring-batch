package com.example.batchrest.email;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Test
    public void testSendFailureNotification() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        EmailService service = new EmailService(mailSender);

        service.sendFailureNotification("Test failure message");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
