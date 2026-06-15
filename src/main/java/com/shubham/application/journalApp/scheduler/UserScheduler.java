package com.shubham.application.journalApp.scheduler;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.repository.UserRepositoryImpl;
import com.shubham.application.journalApp.service.EmailService;
import com.shubham.application.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSentimentAnalysisMail(){
        //Get all the users whom email exists and sentimentAnalysis is true
        List<User> users = userRepository.getUserForSentimentAnalysis();
        //Loop through all the users
        for(User user: users){
            //Get journalEntries of the user
            List<JournalEntry> journalEntries = user.getJournalEntries();
            //Filter out the last 7 days journalEntries Content
            List<String> filteredEntries = journalEntries
                                                        .stream()
                                                        .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                                                        .map(x -> x.getContent())
                                                        .collect(Collectors.toList());
            //combine all the List entries into a single String
            String last7DaysEntries = String.join(" ", filteredEntries);
            //Pass the last7DaysEntries to the sentimentAnalysisService to get the sentiment
            String sentiment = sentimentAnalysisService.getSentiment(last7DaysEntries);
            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", sentiment);

        }
    }
}
