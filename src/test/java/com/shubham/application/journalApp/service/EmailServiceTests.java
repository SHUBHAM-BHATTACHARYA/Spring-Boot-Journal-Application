package com.shubham.application.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;
    @Test
    public void testSendMail(){
        emailService.sendMail("shubham.sahel@gmail.com", "Testing Java Mail Sender Spring Boot", "We are learning sending Mail through Spring Boot");
    }
}
