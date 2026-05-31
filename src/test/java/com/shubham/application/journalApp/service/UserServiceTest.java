package com.shubham.application.journalApp.service;

import com.shubham.application.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "shubham1234",
            "tapan5678",
            "suman9012"
    })
    public void testFindByUsername(String username){
        assertEquals(4, 2+2);
        assertTrue(userRepository.findByUsername(username).isPresent());
    }
}
