package com.shubham.application.journalApp.repository;

import com.shubham.application.journalApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
public class UserRepositoryImplTests {
    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testFindUserBySentimentAnalysis(){
        List<User> users = userRepositoryImpl.getUserForSentimentAnalysis();
        var ListUsername = users.stream().map(User::getUsername).toList();
        log.info("Users = {}", ListUsername);
        assertEquals(ListUsername.contains("shubham1234"), true);
    }
}
