package com.shubham.application.journalApp.controller;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.service.JournalEntryService;
import com.shubham.application.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journalApplication/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/getJournalByUserName")
    public ResponseEntity<?> getAllJournal(){
        try{
            //We are fetching all the journals specific to the user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.getUserByUsername(username).get();
            List<JournalEntry> allJournalEntry = user.getJournalEntries();
            if(allJournalEntry!=null && !allJournalEntry.isEmpty()){
                return new ResponseEntity<>(allJournalEntry, HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getJournalById/{id}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId id){
        try{
            Optional<JournalEntry> journalEntry = journalEntryService.getJournalEntryById(id);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addJournalByUsername")
    public ResponseEntity<?> createJournal(@RequestBody JournalEntry journalEntry){
        try{
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedJournalEntry = journalEntryService.saveEntry(journalEntry);
            //We have to add the new journal body to the user's journalEntries lists
            try{
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userService.getUserByUsername(username).get();
                user.getJournalEntries().add(savedJournalEntry);
                userService.saveUser(user);
                return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/editJournalByJournalId/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry){
        try{
            JournalEntry existingJournal = journalEntryService.getJournalEntryById(id).get();
            if(existingJournal!=null){
                existingJournal.setTitle(newJournalEntry.getTitle());
                existingJournal.setContent(newJournalEntry.getContent());
                existingJournal.setDate(LocalDateTime.now());
                journalEntryService.updateEntry(existingJournal);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteJournalByUsername&JournalId/{username}/{journalId}")
    @Transactional
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId journalId, String username){
        try{
            journalEntryService.deleteEntry(journalId);
            //We have to remove the journal from the user's journalEntries lists
            User user = userService.getUserByUsername(username).get();
            user.getJournalEntries().removeIf(x -> x.getId().equals(journalId));
            userService.saveUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
