package com.shubham.application.journalApp.controller;

import com.shubham.application.journalApp.entity.JournalEntry;
import com.shubham.application.journalApp.entity.User;
import com.shubham.application.journalApp.service.JournalEntryService;
import com.shubham.application.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/journalApplication/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    //Register User can show their journal
    @GetMapping("/getJournalByUserName")
    public ResponseEntity<?> getAllJournal(){
        try{
            //We are fetching all the journals specific to the user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            log.info(username);
            User user = userService.getUserByUsername(username).get();
            List<JournalEntry> allJournalEntry = user.getJournalEntries();
            if(allJournalEntry!=null && !allJournalEntry.isEmpty()){
                return new ResponseEntity<>(allJournalEntry, HttpStatus.OK);
            } else{
                log.error("No Journal Register by User - {}",username);
                return new ResponseEntity<>(String.format("No Journal Register by User - %s", username), HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            log.error("Something was wrong with the Requests");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Register User can show their journal
    @GetMapping("/getJournalByJournalId/{id}")
    public ResponseEntity<?> getJournalByJournalId(@PathVariable ObjectId id){
        try{
            //Fetch the journal by Id
            JournalEntry existingJournal = journalEntryService.getJournalEntryById(id).get();
            if(existingJournal!=null){
                    return new ResponseEntity<>(existingJournal, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Journal not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Register User can add journal
    @PostMapping("/addJournal")
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
                return ResponseEntity.status(HttpStatus.CREATED).body("Journal Added Successfully");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add journal: " + e.getMessage());
        }

    }

    //Register User can edit their journal
    @PutMapping("/editJournalByJournalId/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry){
        try{
            //Fetch the journal by Id
            JournalEntry existingJournal = journalEntryService.getJournalEntryById(id).get();
            if(existingJournal!=null){
                //Authenticate the User
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userService.getUserByUsername(username).get();
                //If the journal belongs to that USER then we can edit
                if(user.getJournalEntries().contains(existingJournal)){
                    existingJournal.setTitle(newJournalEntry.getTitle());
                    existingJournal.setContent(newJournalEntry.getContent());
                    existingJournal.setDate(LocalDateTime.now());
                    journalEntryService.updateEntry(existingJournal);
                    return new ResponseEntity<>("Journal updated successfully",HttpStatus.CREATED);
                } else{
                    return new ResponseEntity<>(String.format("Journal not belong to the User - %s", username), HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("Journal not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Register User can delete their journal
    @DeleteMapping("/deleteJournalByJournalId/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id){
        try{
            //Fetch the journal by Id
            JournalEntry existingJournal = journalEntryService.getJournalEntryById(id).get();
            System.out.print(existingJournal);
            if(existingJournal!=null){
                //Authenticate the User
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userService.getUserByUsername(username).get();
                //If the journal belongs to that USER then we can delete
                if(user.getJournalEntries().contains(existingJournal)){
                    journalEntryService.deleteEntry(id);
                    //We have to remove the journal from the user's journalEntries lists
                    user.getJournalEntries().removeIf(x -> x.getId().equals(id));
                    userService.saveUser(user);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else{
                    return new ResponseEntity<>(String.format("Journal not belong to the User - %s", username), HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity<>("Journal not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
